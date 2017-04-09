package cn.hnisi.mail.iface;

public interface IMailService {
	public abstract void sendMail(IMailConfig config) throws Exception;
}
