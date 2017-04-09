package cn.hnisi.mail.iface;

public interface IMailConfig {
	
	public String getHost();
	public void setHost(String host);
	public String getPort();
	public void setPort(String port);
	public String getFromMail();
	public void setFromMail(String fromMail);
	public String getUser();
	public void setUser(String user);
	public String getPassword();
	public void setPassword(String password);
	public String getToMail();
	public void setToMail(String toMail);
	public String getMailTitle();
	public void setMailTitle(String mailTitle);
	public String getMailContent();
	public void setMailContent(String mailContent);
	
}
