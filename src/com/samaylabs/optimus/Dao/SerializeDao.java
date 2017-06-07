package com.samaylabs.optimus.Dao;

public class SerializeDao {

	/*private Connection connection;
	private final String SQL_SERIALIZE_OBJECT = "INSERT INTO serialized_optimus_objects(object_name, serialized_object) VALUES (?, ?)";
	private final String SQL_DESERIALIZE_OBJECT = "SELECT serialized_object FROM serialized_optimus_objects WHERE serialized_id = ?";
	
	
	public SerializeDao(){
		DbConnection db = new DbConnection();
		connection = db.getConncetion();
	}

	public long serializeJavaObjectToDB(Object objectToSerialize) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(SQL_SERIALIZE_OBJECT,Statement.RETURN_GENERATED_KEYS);

		// just setting the class name
		pstmt.setString(1, objectToSerialize.getClass().getName());
		pstmt.setObject(2, objectToSerialize);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		int serialized_id = -1;
		if (rs.next()) {
			serialized_id = rs.getInt(1);
		}
		rs.close();
		pstmt.close();
		return serialized_id;
	}	
	
	public Object deSerializeJavaObjectFromDB(Connection connection,
			long serialized_id) throws SQLException, IOException,
			ClassNotFoundException {
		PreparedStatement pstmt = connection
				.prepareStatement(SQL_DESERIALIZE_OBJECT,Statement.RETURN_GENERATED_KEYS);
		pstmt.setLong(1, serialized_id);
		ResultSet rs = pstmt.executeQuery();
		rs.next();

		// Object object = rs.getObject(1);

		byte[] buf = rs.getBytes(1);
		ObjectInputStream objectIn = null;
		if (buf != null)
			objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

		Object deSerializedObject = objectIn.readObject();

		rs.close();
		pstmt.close();

		System.out.println("Java object de-serialized from database. Object: "
				+ deSerializedObject + " Classname: "
				+ deSerializedObject.getClass().getName());
		return deSerializedObject;
	}*/
	
}
