package team04.project3.util;

interface LogRecordInterface {
	
    public final String _formattedMessage = "";
    public final String _message="";
  
    /**
     * Gets the formatted message
     * @return The formatted message
     */
    public String getFormattedMessage();

    /**
     * Returns the original log message
     * @return The original log message
     */
    public String getMessage();
    
}
