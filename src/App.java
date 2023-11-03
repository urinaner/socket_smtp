import java.io.*;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import java.util.Base64;

public class App {
    public static void main(String[] args) {
        try {
            String smtpServer = "smtp.naver.com";
            int smtpPort = 465;
            String username = "urinaner@naver.com";
            String password = "wkddudwo123!";

            // 사용자 이름을 UTF-8로 인코딩하여 Base64로 변환
            byte[] usernameBytes = username.getBytes("UTF-8");
            String base64Username = Base64.getEncoder().encodeToString(usernameBytes);

            // 암호를 UTF-8로 인코딩하여 Base64로 변환
            byte[] passwordBytes = password.getBytes("UTF-8");
            String base64Password = Base64.getEncoder().encodeToString(passwordBytes);

            // SSL 보안 소켓 생성 및 서버에 연결
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket socket = socketFactory.createSocket(smtpServer, smtpPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // 서버로부터 환영 메시지 읽기
            String response = reader.readLine();
            System.out.println("서버 응답: " + response);

            // 로그인
            writer.write("HELO urinaner@naver.com\r\n");
            writer.flush(); //flush()는 현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다.
            response = reader.readLine(); 
            System.out.println("서버 응답: " + response);

            // SSL 보안 소켓에 로그인 정보 전송
            writer.write("AUTH LOGIN\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            // 사용자 이름과 비밀번호를 Base64로 인코딩하여 전송
            writer.write(base64Username + "\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            writer.write(base64Password + "\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            // 이메일 데이터 설정
            String sender = "urinaner@naver.com";
            String recipient = "urinaner@naver.com";
            String subject = "Test Email";
            String body = "This is a test email.";

            // 이메일 보내기
            writer.write("MAIL FROM:<" + sender + ">\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            writer.write("RCPT TO:<" + recipient + ">\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            writer.write("DATA\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            // 이메일 내용 전송
            writer.write("Subject: " + subject + "\r\n");
            writer.write("From: " + sender + "\r\n");
            writer.write("To: " + recipient + "\r\n");
            writer.write("\r\n");
            writer.write(body + "\r\n");
            writer.write(".\r\n");
            writer.flush();
            response = reader.readLine();
            System.out.println("서버 응답: " + response);

            // 연결 종료
            writer.write("QUIT\r\n");
            writer.flush();
            response = reader.readLine(); //readLine 파일의 모든 줄을 읽어서 각각의 줄을 요소로 가지는 리스트를 리턴
            System.out.println("서버 응답: " + response);

            // 소켓 및 스트림 닫기
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}