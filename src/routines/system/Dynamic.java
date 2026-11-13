package routines.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Dynamic implements Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 7990658608074365829L;

    public List<DynamicMetadata> metadatas;

    private List<Object> values = new ArrayList<Object>(30);

    private String dbmsId = "";

    public Dynamic() {
        this.metadatas = new ArrayList<DynamicMetadata>();
    }

    public void setDbmsId(String dbmsId) {
        this.dbmsId = dbmsId;
    }

    public String getDbmsId() {
        return this.dbmsId;
    }

    public int getColumnCount() {
        return this.metadatas.size();
    }

    public DynamicMetadata getColumnMetadata(int index) {
        return this.metadatas.get(index);
    }

    public int getIndex(String columnName) {
        for (int i = 0; i < this.getColumnCount(); i++) {
            if (this.metadatas.get(i).getName().equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public Object getColumnValue(int index) {
        if (index < this.metadatas.size()) {
            return values.get(index);
        }
        return null;
    }

    public Object getColumnValue(String columnName) {
        for (int i = 0; i < this.getColumnCount(); i++) {
            if (this.metadatas.get(i).getName().equals(columnName)) {
                return this.getColumnValue(i);
            }
        }
        return null;
    }

    public void addColumnValue(Object value) {
        if (values.size() < metadatas.size())
            values.add(value);
    }

    public void setColumnValue(int index, Object value) {
        if (index < this.metadatas.size()) {
            modifyColunmValue(index, value);
        }
    }

    /**
     * Need to replace the element if the index already exists or add the new element if it does not exist yet.
     *
     * @param index
     * @param value
     */
    private void modifyColunmValue(int index, Object value) {

        if (index < values.size()) {
            values.set(index, value);
        } else if (index < this.metadatas.size()) {
            for (int i = values.size(); i < index; i++) {
                values.add(null);
            }
            values.add(value);
        }
    }

    public void clearColumnValues() {
        values.clear();
    }

    public void writeValuesToStream(java.io.OutputStream out, String delimiter) throws java.io.IOException {
        for (int i = 0; i < metadatas.size(); i++) {
            out.write((String.valueOf(values.get(i))).getBytes());
            if (i != (metadatas.size() - 1))
                out.write(delimiter.getBytes());
        }
        out.flush();
    }

    public void writeHeaderToStream(java.io.OutputStream out, String delimiter) throws java.io.IOException {
        for (int i = 0; i < metadatas.size(); i++) {
            out.write((String.valueOf(metadatas.get(i).getName())).getBytes());
            if (i != (metadatas.size() - 1))
                out.write(delimiter.getBytes());
        }
        out.flush();
    }

    public static String getTalendTypeFromDBType(String dbName, String dbType, int length, int precision) {
        // TODO:: replace this fucntion with full implementation with XML
        // mappings
        String talendType = null;
        if (dbType.contains("(")) {
            dbType = dbType.substring(0, dbType.indexOf('('));
        }

        talendType = null;
        if (talendType == null) {
            talendType = "id_String";
        }

        return talendType;
    }

    /**
     * Comment method "getDBTypeFromTalendType"<br>
     * generate and return a string composed of DB type, length and precision, such
     * as:int(4),String(200),DATE,DECIMAL(20,4) .
     *
     * @author talend
     * @param dbmsId
     * @param talendType
     * @param length
     * @param precision
     * @return
     */
    public static String getDBTypeFromTalendType(String dbmsId, String talendType, int length, int precision) {

        String dbmsType = "id_String";
        return dbmsType;
    }

    public String toString() {
        return this.toString(" - ");
    }

    public String toString(String delimiter) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < metadatas.size(); i++) {
            DynamicMetadata metadata = metadatas.get(i);
            out.append((String.valueOf(values.get(i))));
            if (i != (metadatas.size() - 1))
                out.append(delimiter);
        }
        return out.toString();
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        boolean b = true;
        Dynamic D = (Dynamic) obj;
        if (this.metadatas.size() != D.metadatas.size()) {
            b = false;
        } else {
            for (int i = 0; i < this.metadatas.size(); i++) {
                if (!(this.metadatas.get(i).equals(D.metadatas.get(i)))) {
                    b = false;
                }
            }
        }
        if (!b) {
            return b;
        }
        if (this.values.size() != D.values.size()) {
            b = false;
        } else {
            b = this.values.equals(D.values);
        }
        return b;
    }

    public Dynamic clone() {
        return clone(false);
    }

    public Dynamic clone(boolean isCloneMetadata) {
        Dynamic dynamic = new Dynamic();
        dynamic.dbmsId = this.dbmsId;
        for (int i = 0; i < this.metadatas.size(); i++) {
            if (isCloneMetadata) {
                dynamic.metadatas.add(this.metadatas.get(i).clone());
            } else {
                dynamic.metadatas.add(this.metadatas.get(i));
            }
        }
        for (int j = 0; j < this.values.size(); j++) {
            dynamic.values.add(values.get(j));
        }
        return dynamic;
    }

    public boolean contains(String columnName) {

        for (int i = 0; i < this.getColumnCount(); i++) {
            if (columnName.equals(this.getColumnMetadata(i).getName())) {
                return true;
            }
        }
        return false;
    }

    public routines.system.Dynamic copyMetadata() {
        final Dynamic copy = new Dynamic();
        copy.dbmsId = this.dbmsId;
        copy.metadatas = this.metadatas;
        return copy;
    }

    public routines.system.Dynamic copy() {

        routines.system.Dynamic dynamicTarget = new routines.system.Dynamic();

        dynamicTarget.dbmsId = this.dbmsId;

        for (int i = 0; i < this.getColumnCount(); i++) {
            dynamicTarget.metadatas.add(this.metadatas.get(i));
            dynamicTarget.addColumnValue(this.getColumnValue(i));
        }
        return dynamicTarget;
    }

    public routines.system.Dynamic merge(routines.system.Dynamic dynamicSource) {
        routines.system.Dynamic dynamicTarget = new routines.system.Dynamic();
        dynamicTarget = this.copy();

        for (int i = 0; i < dynamicSource.getColumnCount(); i++) {
            if (!this.contains(dynamicSource.metadatas.get(i).getName()))
                dynamicTarget.metadatas.add(dynamicSource.metadatas.get(i));
            dynamicTarget.addColumnValue(dynamicSource.getColumnValue(i));
        }
        return dynamicTarget;
    }

    public void removeDynamicElement(String columnName) {
        if (columnName != null) {
            for (int i = 0; i < this.getColumnCount(); i++) {
                if (columnName.equalsIgnoreCase(this.metadatas.get(i).getName())) {
                    removeDynamicElement(i);
                }
            }
        }
    }

    public void removeDynamicElement(int index) {
        if (index < getColumnCount()) {
            metadatas.remove(index);
            values.remove(index);
        }
    }

    /**
     * Removes from Dynamic field column with the columnName (ignoring case)
     * Drop data value for this field
     *
     * @param columnName name of column to remove
     * @return cloned object with dropped column
     */
    public Dynamic removeColumn(String columnName) {
        Dynamic newDynamicColumn = this.clone();
        newDynamicColumn.removeDynamicElement(columnName);

       return newDynamicColumn;
    }

    /**
     * Removes from Dynamic field all columns which names (ignoring case) are present in columnNames
     * Drop data values for this fields
     *
     * @param columnNames names collection of columns to remove
     * @return cloned object with dropped columns
     */
    public Dynamic removeColumns(Collection<String> columnNames) {
        Dynamic newDynamicColumn = this.clone();
        Set<String> columnNamesLowerCase = getColumnNamesInLowerCase(columnNames);

        for (int i = getColumnCount() - 1; i >= 0; i--) {
            if (columnNamesLowerCase.contains(this.metadatas.get(i).getName().toLowerCase())) {
                newDynamicColumn.removeDynamicElement(i);
            }
        }

        return newDynamicColumn;
    }

    /**
     * Do the same as {@link #removeColumns(Collection)}
     *
     */

    public Dynamic removeColumns(String... columnNames) {
        return removeColumns(Arrays.asList(columnNames));
    }

    /**
     * Removes from Dynamic field all columns which names are matched to pattern Drop data values for this fields
     *
     * Pattern is case sensitive, for delete rows with ignoring case regex, add '(?)' in the begin of your pattern
     *
     * @param pattern regex
     * @return cloned object with dropped columns
     */
    public Dynamic removeColumnsByRegex(String pattern) {
        Dynamic newDynamicColumn = this.clone();
        Pattern columnNamePattern = Pattern.compile(pattern);
        for (int i = metadatas.size() - 1; i >= 0; i--) {
            if (columnNamePattern.matcher(metadatas.get(i).getName()).matches()) {
                newDynamicColumn.removeDynamicElement(i);
            }
        }
        return newDynamicColumn;
    }

    /**
     * Removes from Dynamic field all columns which talend type is matched to columnType Drop data values for this
     * fields
     *
     * @param columnType one of Talend type
     * @return cloned object with dropped columns
     */
    public Dynamic removeColumnsByType(String columnType) {
        Dynamic newDynamicColumn = this.clone();
        for (int i = metadatas.size() - 1; i >= 0; i--) {
            if (columnType.equals(metadatas.get(i).getType())) {
                newDynamicColumn.removeDynamicElement(i);
            }
        }

        return newDynamicColumn;
    }

    /**
     * Add dynamic column in the end of Dynamic Schema, set specified default value
     *
     * @param newColumn previously created metadata with all parameters such as type, nullable etc.
     * @param defaultValue Object which is set as value to new column
     * @return cloned object with added column
     */
    public Dynamic addColumn(DynamicMetadata newColumn, Object defaultValue) {
        return addColumn(this.getColumnCount(), newColumn, defaultValue);
    }


    /**
     * Insert dynamic column at the specified position in Dynamic Schema, set specified default value
     *
     * @param index index at which the specified element is to be inserted
     * @param newColumn previously created metadata with all parameters such as type, nullable etc.
     * @param defaultValue Object which is set as value to new column
     *
     * @throws IllegalArgumentException if the index is out of range
     * (<tt>index &lt; 0 || index &gt; size()</tt>)
     * @return cloned object with added column
     */
    public Dynamic addColumn(int index, DynamicMetadata newColumn, Object defaultValue) {
        if (contains(newColumn.getName())) {
            return this;
        }

        Dynamic newDynamicColumn = this.clone();
        if (index <= this.getColumnCount()) {
            newDynamicColumn.metadatas.add(index, newColumn);
            newDynamicColumn.values.add(index, defaultValue);
        } else {
            throw new IllegalArgumentException("Incorrect index value " + index + " for dynamic field size " + this.getColumnCount());
        }

        return newDynamicColumn;
    }

    /**
     * Add dynamic column in the end of Dynamic Schema, set java default value
     *
     * @param newColumn previously created metadata with all parameters such as type, nullable etc.
     * @return cloned object with added column
     */
    public Dynamic addColumn(DynamicMetadata newColumn) {
        return addColumn(this.getColumnCount(), newColumn);
    }

    /**
     * Create and add dynamic column in the end of Dynamic Schema, set java default value
     * If column with this colunnName already exists - do nothing
     *
     * @param columnName name of dynamic field inside schema
     * @param talendType talend's column type such as 'id_String' or 'id_Double'
     * @param isNullable boolean show if column could have null values
     * @return cloned object with added column
     */
    public Dynamic addColumn(String columnName, String talendType, boolean isNullable) {
        return addColumn(this.getColumnCount(), columnName, talendType, isNullable);
    }

    /**
     * Create and insert dynamic column at the specified position in Dynamic Schema, set java default value
     *
     * @param index index at which the specified element is to be inserted
     * @param columnName name of dynamic field inside schema
     * @param talendType talend's column type such as 'id_String' or 'id_Double'
     * @param isNullable boolean show if column could have null values
     * @throws IllegalArgumentException if the index is out of range
     * (<tt>index &lt; 0 || index &gt; size()</tt>)
     * @return cloned object with added column
     */
    public Dynamic addColumn(int index, String columnName, String talendType, boolean isNullable) {
        return addColumn(index, createDynamicMetadata(columnName, talendType, isNullable));
    }

    /**
     * Create and insert dynamic column in the end of Dynamic Schema, set specified default value
     *
     * @param columnName name of dynamic field inside schema
     * @param talendType talend's column type such as 'id_String' or 'id_Double'
     * @param defaultValue Object which is set as value to new column
     * @param isNullable boolean show if column could have null values
     * @return cloned object with added column
     */
    public Dynamic addColumn(String columnName, String talendType, Object defaultValue, boolean isNullable) {
        return addColumn(this.getColumnCount(), columnName, talendType, defaultValue, isNullable);
    }

    /**
     * Create and insert dynamic column at the specified position in Dynamic Schema, set specified default value
     *
     * @param index index at which the specified element is to be inserted
     * @param columnName name of dynamic field inside schema
     * @param talendType talend's column type such as 'id_String' or 'id_Double'
     * @param defaultValue Object which is set as value to new column
     * @param isNullable boolean show if column could have null values
     *
     * * @throws IllegalArgumentException if the index is out of range
     * (<tt>index &lt; 0 || index &gt; size()</tt>)
     * @return cloned object with added column
     */
    public Dynamic addColumn(int index, String columnName, String talendType, Object defaultValue, boolean isNullable) {
        return addColumn(index, createDynamicMetadata(columnName, talendType, isNullable), defaultValue);
    }

    /**
     * Insert dynamic column at the specified position in Dynamic Schema, set java default value
     *
     * @param newColumn previously created metadata with all parameters such as type, nullable etc.
     * @throws IllegalArgumentException when newColumn is not Nullable and not primitive, use
     * {@link #addColumn(int, DynamicMetadata, Object)} instead
     */
    public Dynamic addColumn(int index, DynamicMetadata newColumn) {
        if (newColumn.isNullable()) {
            return addColumn(index, newColumn, null);
        } else  {
            return addColumn(index, newColumn, getDefaultJavaValue(newColumn.getType()));
        }
    }



    public Set<String> getAllColumnNames() {
        Set<String> columnNames = new HashSet<>();

        for (DynamicMetadata column: metadatas) {
            columnNames.add(column.getName());
        }
        return columnNames;
    }

    /**
    * Modify value of column, which name equals (ignoring case) columnName
    * New column value is got with applying @see{@link UnaryOperator#apply(Object)}}
    *
    * @param function implementation of @see{@link java.util.function.UnaryOperator}} interface
    * @param columnName name of dynamic column inside Dynamic Schema
    * @return cloned object with changed column value
    */
    public <T> Dynamic applyFunction(UnaryOperator<T> function, String columnName) {
        Dynamic newDynamicColumn = this.clone();
        for (int i = 0; i < newDynamicColumn.getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(metadatas.get(i).getName())) {
                T oldValue = (T) newDynamicColumn.getColumnValue(i);
                newDynamicColumn.setColumnValue(i, function.apply(oldValue));
            }
        }

        return newDynamicColumn;
    }

    /**
     * Modify value of columns, which talend type is equals to talendType parameter
     * New column values are got with applying @see{@link UnaryOperator#apply(Object)}}
     *
     * @param function implementation of @see{@link java.util.function.UnaryOperator}} interface
     * @param talendType name of Talend type
     * @return cloned object with changed columns values
     */
    public <T> Dynamic applyFunctionByType(UnaryOperator<T> function, String talendType) {
        Dynamic newDynamicColumn = this.clone();
        for (int i = 0; i < newDynamicColumn.getColumnCount(); i++) {
            if (talendType.equals(newDynamicColumn.metadatas.get(i).getType())) {
                T oldValue = (T) newDynamicColumn.getColumnValue(i);
                newDynamicColumn.setColumnValue(i, function.apply(oldValue));
            }
        }
        return newDynamicColumn;
    }

    /**
     * Modify value of columns, which column names are equals to regex pattern
     * Pattern is case sensitive, for applying function with ignoring case regex, add '(?)' in the begin of your pattern
     * New column values are got with applying @see{@link UnaryOperator#apply(Object)}}
     *
     * @param function implementation of @see{@link java.util.function.UnaryOperator}} interface
     * @param pattern
     * @return cloned object with changed columns values
     */
    public <T> Dynamic applyFunctionByRegex(UnaryOperator<T> function, String pattern) {
        Dynamic newDynamicColumn = this.clone();
        Pattern columnNamePattern = Pattern.compile(pattern);
        for (int i = 0; i < newDynamicColumn.getColumnCount(); i++) {
            if (columnNamePattern.matcher(newDynamicColumn.metadatas.get(i).getName()).matches()) {
                T oldValue = (T) newDynamicColumn.getColumnValue(i);
                newDynamicColumn.setColumnValue(i, function.apply(oldValue));
            }
        }
        return newDynamicColumn;
    }

    /**
     * Modify value of columns, which names are present (ignoring case) in columnNames collection
     * New column value for every column is got with applying @see{@link UnaryOperator#apply(Object)}}
     *
     * @param function implementation of @see{@link java.util.function.UnaryOperator}} interface
     * @param columnNames names of dynamic columns inside Dynamic schema to apply function
     * @return cloned object with changed columns values
     */
    public <T> Dynamic applyFunctionOnColumns(UnaryOperator<T> function, Collection<String> columnNames) {
        Dynamic newDynamicColumn = this.clone();
        Set<String> columnNamesInLowerCase = getColumnNamesInLowerCase(columnNames);

        for (int i = 0; i < newDynamicColumn.getColumnCount(); i++) {
            if (columnNamesInLowerCase.contains(newDynamicColumn.metadatas.get(i).getName().toLowerCase())) {
                T oldValue = (T) newDynamicColumn.getColumnValue(i);
                newDynamicColumn.setColumnValue(i, function.apply(oldValue));
            }
        }
        return newDynamicColumn;
    }

    /**
     * Do the same as {@link #applyFunctionOnColumns(UnaryOperator, Collection)}
     *
     */
    public <T> Dynamic applyFunctionOnColumns(UnaryOperator<T> function, String... columnNames) {
        return applyFunctionOnColumns(function, Arrays.asList(columnNames));
    }

    private static Set<String> getColumnNamesInLowerCase(Collection<String> columnNames) {
        return columnNames.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    /**
     * This method returns default value for not nullable columns with primitive java type
     *
     * @param primitiveType Talend "id_JavaType"
     * @return default java value for primitive
     * @throws IllegalArgumentException when type could not be primitive
     */
    private static Object getDefaultJavaValue(String primitiveType) {
        switch(primitiveType) {
        case "id_Character": return ' ';
        case "id_Boolean": return false;
        case "id_Float": return 0.0f;
        case "id_Double": return 0.0;
        case "id_Long":
        case "id_Integer":
        case "id_Short":
        case "id_Byte": return 0;
        default: throw new IllegalArgumentException("Not nullable column with not primitive type should have default value");
        }
    }

    /**
     * Creates and returns DynamicMetadata for column, using name, type and isNullable parameters
     *
     * @param columnName column name
     * @param talendType talend's column type such as 'id_String' or 'id_Double'
     * @param isNullable boolean show if column could have null values
     * @return new DynamicMetadata object
     */
    private static DynamicMetadata createDynamicMetadata(String columnName, String talendType, boolean isNullable) {
        DynamicMetadata column = new DynamicMetadata();
        column.setName(columnName);
        column.setType(talendType);
        column.setNullable(isNullable);
        return column;
    }

}

