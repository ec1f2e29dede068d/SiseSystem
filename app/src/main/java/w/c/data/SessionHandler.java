package w.c.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SessionHandler {
    private String cookie = "";
    private String username;
    private String password;

    public SessionHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 登录
     *
     * @return 登录是否成功
     */
    public boolean login() {
        boolean ifLoginSuccess = false;
        try {
            if (!this.username.equals("") && !this.password.equals("")) {
                //获取cookie
                StringBuilder result = new StringBuilder();
                URL url = new URL("http://class.sise.com.cn:7001/sise/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                this.cookie = connection.getHeaderField("Set-Cookie");
                int semicolonIndex = this.cookie.indexOf(";");
                this.cookie = this.cookie.substring(0, semicolonIndex);

                //获取页面表单元素name，value值
                InputStream stream = connection.getInputStream();
                if (stream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(stream, "GBK");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String context;
                    while ((context = bufferedReader.readLine()) != null) {
                        result.append(context).append("\n");
                    }
//                    System.out.println(result.toString());
//                    System.out.println("--------------------------------------------------------------");
                    stream.close();
                    connection.disconnect();
                }
                Document document = Jsoup.parse(result.toString());
                Elements elements = document.getElementsByTag("input");
                Element element = elements.get(0);
                String ipMd5Value = element.attr("name");
                String ipAndSiseMd5Value = element.attr("value");

                //发起登录请求
                String postData = ipMd5Value + "=" + URLEncoder.encode(ipAndSiseMd5Value, "GBK")
                        + "&username=" + URLEncoder.encode(username, "GBK")
                        + "&password=" + URLEncoder.encode(password, "GBK");
                byte[] data = postData.getBytes();
                url = new URL("http://class.sise.com.cn:7001/sise/login_check_login.jsp");
                HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
                connection2.setDoOutput(true);
                connection2.setRequestProperty("Cookie", cookie);
                connection2.setRequestMethod("POST");
                OutputStream postStream = connection2.getOutputStream();
                postStream.write(data);
                postStream.flush();

                /*利用正则表达式判断是否登录成功
                use regular expression judge if login success.*/
                InputStream stream2 = connection2.getInputStream();
                result = new StringBuilder();
                if (stream2 != null) {
                    // Converts Stream to String with max length of 500.
                    InputStreamReader inputStreamReader = new InputStreamReader(stream2, "GBK");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String context;
                    while ((context = bufferedReader.readLine()) != null) {
                        result.append(context);
                    }
//                    System.out.println(result.toString());
//                    System.out.println("--------------------------------------------------------------");
                    stream2.close();
                    postStream.close();
                    connection2.disconnect();
                }
                String successLabel = ".*index.jsp.*";
                ifLoginSuccess = Pattern.matches(successLabel, result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifLoginSuccess;
    }

    //首页获取当前学期课程表
    public ArrayList<String> getSyllabus() {
        ifSessionWasOverdue();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            URL url = new URL("http://class.sise.com.cn:7001/sise/module/student_schedular/student_schedular.jsp");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Cookie", this.cookie);
            httpURLConnection.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            String context;
            while ((context = bufferedReader.readLine()) != null) {
                result.append(context).append("\n");
            }
//            System.out.println(result.toString());
//            System.out.println("--------------------------------------------------------------");
            httpURLConnection.disconnect();
            Document document = Jsoup.parse(result.toString());

            Elements elements1 = document.getElementsByClass("font12");
            for (Element element2 : elements1) {
                arrayList.add(element2.text());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    //根据学年和学期获取当前学期课程表
    public ArrayList<String> getSyllabus(int schoolYear, int semester) {
        ifSessionWasOverdue();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            URL url = new URL("http://class.sise.com.cn:7001/sise/module/student_schedular/student_schedular.jsp?schoolyear=" + schoolYear + "&semester=" + semester);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Cookie", this.cookie);
            httpURLConnection.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            String context;
            while ((context = bufferedReader.readLine()) != null) {
                result.append(context).append("\n");
            }
//            System.out.println(result.toString());
//            System.out.println("--------------------------------------------------------------");
            httpURLConnection.disconnect();

            Document document = Jsoup.parse(result.toString());
            Elements elements1 = document.getElementsByClass("font12");
            for (Element element2 : elements1) {
                arrayList.add(element2.text());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 注销登录
     * logout from system
     */
    public void logOut() {
        ifSessionWasOverdue();
        if (!(cookie == null)) {
            try {
                URL url = new URL("http://class.sise.com.cn:7001/sise/common/logout.jsp");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.addRequestProperty("Cookie", this.cookie);
                httpURLConnection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();
                String context;
                while ((context = bufferedReader.readLine()) != null) {
                    result.append(context).append("\n");
                }
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
                httpURLConnection.disconnect();
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
            cookie = "";
        }
    }

    /**
     * 获取主页各url
     * get every url of index content
     */
    public void getIndexContent() {
        ifSessionWasOverdue();
        if (!(cookie == null)) {
            try {
                URL url = new URL("http://class.sise.com.cn:7001/sise/module/student_states/student_select_class/main.jsp");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.addRequestProperty("Cookie", this.cookie);
                httpURLConnection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();
                String context;
                while ((context = bufferedReader.readLine()) != null) {
                    result.append(context).append("\n");
                }
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
                httpURLConnection.disconnect();

                //获取主页各版块html
                Document document = Jsoup.parse(result.toString());
                Elements elements = document.getElementsByClass("table1");
                Element element = elements.get(0);
                Elements elements1 = element.getElementsByAttribute("onclick");

                //获取个人信息成绩url
                String personalInfoUrl = elements1.get(0).attr("onclick");
                int indexOfFullStop = personalInfoUrl.lastIndexOf(".", personalInfoUrl.length() - 45);
                personalInfoUrl = personalInfoUrl.substring(indexOfFullStop + 1, personalInfoUrl.length() - 1);
                personalInfoUrl = "http://class.sise.com.cn:7001" + personalInfoUrl;
                this.personalInfoUrl = new URL(personalInfoUrl);

                //获取考试时间url
                String testTimeUrl = elements1.get(2).attr("onclick");
                indexOfFullStop = testTimeUrl.lastIndexOf(".", testTimeUrl.length() - 39);
                testTimeUrl = testTimeUrl.substring(indexOfFullStop + 1, testTimeUrl.length() - 1);
                testTimeUrl = "http://class.sise.com.cn:7001" + testTimeUrl;
                this.testTimeUrl = new URL(testTimeUrl);

                //获取考勤url
                String attendanceUrl = elements1.get(3).attr("onclick");
                indexOfFullStop = attendanceUrl.lastIndexOf(".", attendanceUrl.length() - 81);
                attendanceUrl = attendanceUrl.substring(indexOfFullStop + 1, attendanceUrl.length() - 1);
                attendanceUrl = "http://class.sise.com.cn:7001" + attendanceUrl;
                this.attendanceUrl = new URL(attendanceUrl);

                //获取奖惩记录url
                String rewardsAndPunishmentsUrl = elements1.get(5).attr("onclick");
                int indexOfComma = rewardsAndPunishmentsUrl.indexOf("'");
                rewardsAndPunishmentsUrl = rewardsAndPunishmentsUrl.substring(indexOfComma + 1, rewardsAndPunishmentsUrl.length() - 1);
                rewardsAndPunishmentsUrl = "http://class.sise.com.cn:7001" + rewardsAndPunishmentsUrl;
                this.rewardsAndPunishmentsUrl = new URL(rewardsAndPunishmentsUrl);

                //获取开设课程url
                String openCourseUrl = elements1.get(10).attr("onclick");
                indexOfComma = openCourseUrl.indexOf("'");
                openCourseUrl = openCourseUrl.substring(indexOfComma + 1, openCourseUrl.length() - 1);
                openCourseUrl = "http://class.sise.com.cn:7001" + openCourseUrl;
                this.openCourseUrl = new URL(openCourseUrl);

                //获取晚归、违规用电记录url
                String violationUrl = elements1.get(21).attr("onclick");
                indexOfFullStop = violationUrl.lastIndexOf(".", violationUrl.length() - 87);
                violationUrl = violationUrl.substring(indexOfFullStop + 1, violationUrl.length() - 1);
                violationUrl = "http://class.sise.com.cn:7001" + violationUrl;
                this.violationUrl = new URL(violationUrl);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取个人信息和成绩
     * get personal information and score
     */
    private URL personalInfoUrl;

    public HashMap<String, ArrayList<String>> getPersonalInfoAndScore() {
        ifSessionWasOverdue();
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        ArrayList<String> personalInfo = new ArrayList<>();
        ArrayList<String> score = new ArrayList<>();
        ArrayList<String> electiveScore = new ArrayList<>();
        if (!(cookie == null)) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) personalInfoUrl.openConnection();
                httpURLConnection.addRequestProperty("Cookie", this.cookie);
                httpURLConnection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();
                String context;
                while ((context = bufferedReader.readLine()) != null) {
                    result.append(context).append("\n");
                }
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
                httpURLConnection.disconnect();
                Document document = Jsoup.parse(result.toString());

                //获取个人信息字符串
                Element element = document.getElementsByTag("table").get(2);
                Elements elements = element.getElementsByClass("td_left");
                personalInfo.add(elements.get(0).child(0).html());
                personalInfo.add(elements.get(1).child(0).html());
                personalInfo.add(elements.get(2).child(0).html());
                personalInfo.add(elements.get(3).child(0).html());
                personalInfo.add(elements.get(4).child(0).html());
                personalInfo.add(elements.get(5).child(0).html());
                personalInfo.add(elements.get(8).html());
                personalInfo.add(elements.get(9).child(0).html());
                personalInfo.add(elements.get(10).child(0).html());

                //获取成绩字符串
                element = document.getElementsByTag("table").get(6);
                elements = element.getElementsByTag("td");
                for (int i = 0; i < elements.size(); i++) {
                    if (i % 10 == 2) {
                        score.add(elements.get(i).child(0).html());
                    } else {
                        score.add(elements.get(i).html());
                    }
                }

                element = document.getElementsByTag("table").get(9);
                elements = element.getElementsByTag("td");
                for (int i = 0; i < elements.size(); i++) {
                    if (i % 9 == 1) {
                        electiveScore.add(elements.get(i).child(0).html());
                    } else {
                        electiveScore.add(elements.get(i).html());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hashMap.put("personalInfo", personalInfo);
        hashMap.put("score", score);
        hashMap.put("electiveScore", electiveScore);
        return hashMap;
    }

    /**
     * 获取考勤信息
     */
    private URL attendanceUrl;

    public ArrayList<String> getAttendance(int pattern) {
        ifSessionWasOverdue();
        ArrayList<String> attendance = new ArrayList<>();
        if (pattern == 0) {
            if (!(cookie == null)) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) attendanceUrl.openConnection();
                    httpURLConnection.addRequestProperty("Cookie", this.cookie);
                    httpURLConnection.setRequestMethod("GET");
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder result = new StringBuilder();
                    String context;
                    while ((context = bufferedReader.readLine()) != null) {
                        result.append(context).append("\n");
                    }
//                    System.out.println(result.toString());
//                    System.out.println("--------------------------------------------------------------");
                    httpURLConnection.disconnect();
                    Document document = Jsoup.parse(result.toString());

                    //获取考勤字符串
                    Element element = document.getElementById("table1");
                    Elements elements = element.getElementsByTag("td");
                    for (int i = 0; i < elements.size(); i++) {
                        if (i % 3 == 2) {
                            String string = elements.get(i).html();
                            if (string.equals("") || string.contains("全勤")) {
                                attendance.add(string);
                            } else if (elements.get(i).child(0).html() != null && !(elements.get(i).child(0).html().equals(""))) {
                                string = string.substring(0, 2) + elements.get(i).child(0).html() + string.substring(string.length() - 19, string.length() - 18);
                                attendance.add(string);
                            } else {
                                attendance.add(string);
                            }

                        } else {
                            attendance.add(elements.get(i).html());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (pattern == 1) {

        }

        return attendance;
    }

    /**
     * 获取考试时间信息
     */
    private URL testTimeUrl;

    public ArrayList<String> getTestTime() {
        ifSessionWasOverdue();
        ArrayList<String> testTime = new ArrayList<>();
        if (!(cookie == null)) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) testTimeUrl.openConnection();
                httpURLConnection.addRequestProperty("Cookie", this.cookie);
                httpURLConnection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();
                String context;
                while ((context = bufferedReader.readLine()) != null) {
                    result.append(context).append("\n");
                }
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
                httpURLConnection.disconnect();
                Document document = Jsoup.parse(result.toString());

                //获取考试时间字符串
                Elements elements = document.getElementsByAttributeValue("class", "table");
                Element element = elements.get(0);
                elements = element.getElementsByTag("td");
                for (Element element1 : elements) {
                    testTime.add(element1.html());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return testTime;
    }

    /**
     * 获取奖惩情况信息
     */
    private URL rewardsAndPunishmentsUrl;

    /**
     * 获取开设课程信息
     */
    private URL openCourseUrl;

    /**
     * 获取晚归、违规电器信息
     */
    private URL violationUrl;

    public ArrayList<String> getViolation() {
        ifSessionWasOverdue();
        ArrayList<String> violation = new ArrayList<>();
        if (!(cookie == null)) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) violationUrl.openConnection();
                httpURLConnection.addRequestProperty("Cookie", this.cookie);
                httpURLConnection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();
                String context;
                while ((context = bufferedReader.readLine()) != null) {
                    result.append(context).append("\n");
                }
//                System.out.println(result.toString());
//                System.out.println("--------------------------------------------------------------");
                httpURLConnection.disconnect();
                Document document = Jsoup.parse(result.toString());

                //获取晚归、违规电器字符串
                Elements elements = document.getElementsByAttributeValue("class", "table");
                Element element = elements.get(0);
                elements = element.getElementsByTag("td");
                for (Element element1 : elements) {
                    if (element1.html().indexOf("<") == 0) {
                        violation.add(element1.child(0).html());
                    } else {
                        violation.add(element1.html());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return violation;
    }

    /**
     * 判断会话是否过期，并进行处理
     */
    public void ifSessionWasOverdue() {
        try {
            URL url = new URL("http://class.sise.com.cn:7001/sise/module/student_states/student_select_class/main.jsp");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("Cookie", this.cookie);
            httpURLConnection.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            String context;
            while ((context = bufferedReader.readLine()) != null) {
                result.append(context);
            }
//            System.out.println(result.toString());
//            System.out.println("--------------------------------------------------------------");
            httpURLConnection.disconnect();
            String overdueLabel = ".*login.jsp.*";
            if (Pattern.matches(overdueLabel, result)) {
                login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
