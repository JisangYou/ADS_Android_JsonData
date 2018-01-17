# ADS04 Android

## 수업 내용

- Json을 통해 openApi 데이터 받아오기
- gson, glide library 학습

## Code Review

### MainActivity

```Java
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        load();
    }

    private void load(){
        // data 변수에 불러온 데이터를 입력
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String str = Remote.getData("https://api.github.com/users");
                return str;
            }
            @Override
            protected void onPostExecute(String jsonString) {
                // jsonString 을 parsing
                data = parse(jsonString);
                setList();
            }
        }.execute();
    }

    private List<User> parse(String string){
        Gson gson = new Gson();
        User user[] = gson.fromJson(string, User[].class);
        List<User> result = Arrays.asList(user); //array를 List로 변환
        return result;
    }

    List<User> data;
    private void setList(){
        ListAdapter adapter = new ListAdapter(data, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
```

### ListAdapter

```Java
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder>{
    Context context;
    List<User> data;
    public ListAdapter(List<User> data, Context context){
        this.data = data;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = data.get(position);
        //holder.imageView.setImageURI();
        holder.textId.setText(user.getId()+"");
        holder.textLogin.setText(user.getLogin());
        // 이미지 불러오기
        Glide.with(context)                 // 글라이드 초기화
                .load(user.getAvatar_url()) // 주소에서 이미지 가져오기
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textId;
        TextView textLogin;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textId = itemView.findViewById(R.id.textId);
            textLogin = itemView.findViewById(R.id.textLogin);
        }
    }
}
```

### User

```Java
public class User {
    // 위에 세개만 일단은 사용
    int id;
    String login;
    String avatar_url;

    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getAvatar_url() {
        return avatar_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    String gravatar_id;
    String url;
    String html_url;
    String followers_url;
    String following_url;
    String gists_url;
    String starred_url;
    String subscriptions_url;
    String organizations_url;
    String repos_url;
    String events_url;
    String received_events_url;
    String type;
    boolean site_admin;
}
```

### Remote

```Java
public class Remote {

    public static String getData(String string){
        final StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(string);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            // 통신이 성공인지 체크
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 여기서 부터는 파일에서 데이터를 가져오는 것과 동일
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    result.append(temp).append("\n");
                }
                br.close();
                isr.close();
            } else {
                Log.e("ServerError", con.getResponseCode()+"");
            }
            con.disconnect();
        }catch(Exception e){
            Log.e("Error", e.toString());
        }
        return result.toString();
    }
}
```


## 보충설명

### Json이란?

>> JSON(제이슨[1], JavaScript Object Notation)은 속성-값 쌍으로 이루어진 데이터 오브젝트를 전달하기 위해 인간이 읽을 수 있는 텍스트를 사용하는 개방형 표준 포맷이다. 비동기 브라우저/서버 통신 (AJAX)을 위해, 넓게는 XML(AJAX가 사용)을 대체하는 주요 데이터 포맷이다. 특히, 인터넷에서 자료를 주고 받을 때 그 자료를 표현하는 방법으로 알려져 있다. 자료의 종류에 큰 제한은 없으며, 특히 컴퓨터 프로그램의 변수값을 표현하는 데 적합하다.
>> 본래는 자바스크립트 언어로부터 파생되어 자바스크립트의 구문 형식을 따르지만 언어 독립형 데이터 포맷이다. 즉, 프로그래밍 언어나 플랫폼에 독립적이므로, 구문 분석 및 JSON 데이터 생성을 위한 코드는 C, C++, C#, 자바, 자바스크립트, 펄, 파이썬 등 수많은 프로그래밍 언어에서 쉽게 이용할 수 있다.
>> JSON 포맷은 본래 더글라스 크록포드가 규정하였다. RFC 7159와 ECMA-404라는 두 개의 경쟁 표준에 의해 기술되고 있다. ECMA 표준은 문법만 정의할 정도로 최소한으로만 정의되어 있는 반면 RFC는 시맨틱, 보안적 고려 사항을 일부 제공하기도 한다.[2] JSON의 공식 인터넷 미디어 타입은 application/json이며, JSON의 파일 확장자는 .json이다.

- 예제
```JSON
 {
     "이름": "테스트",
     "나이": 25,
     "성별": "여",
     "주소": "서울특별시 양천구 목동",
     "특기": ["농구", "도술"],
     "가족관계": {"#": 2, "아버지": "홍판서", "어머니": "춘섬"},
     "회사": "경기 수원시 팔달구 우만동"
  }

```
- [JSON 참고자료](https://ko.wikipedia.org/wiki/JSON)
- ![Json Schema](https://c2.staticflickr.com/2/1603/26106186415_407e7cc83a_c.jpg)

### parsing 및 parser

>> 파싱((syntactic) parsing)은 일련의 문자열을 의미있는 토큰(token)으로 분해하고 이들로 이루어진 파스 트리(parse tree)를 만드는 과정을 말한다.
>> 파서(parser)는 인터프리터나 컴파일러의 구성 요소 가운데 하나로, 입력 토큰에 내재된 자료 구조를 빌드하고 문법을 검사한다. 파서는 일련의 입력 문자로부터 토큰을 만들기 위해 별도의 낱말 분석기를 이용하기도 한다. 파서는 수작업으로 프로그래밍되며 도구에 의해 (일부 프로그래밍 언어에서) (반)자동적으로 만들어질 수 있다.

### GSON

- Gson은 java객체를 JSON 표현식으로 변환할 수 있게 하는 Java 라이브러리

1. Json형식으로 변환

```Java
Gson gson = new Gson();
HashMap<String,Object> result = new HashMap<String,Object>();
result.put("result","true");
List<String> list = new ArrayList<String>();    
result.put("list",list);
String json = gson.toJson(result);
```
- Json

``` Json

{
    "name": "hong",
    "age": "24"
}
```

- DTO (VO)

```java
public class UserDTO {
    
    private String name;
    private String age;
 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
}
```

2. String형식에 json을 map으로 변환

```Java
Gson gson = new Gson();
Map<String,String> map = new HashMap<String,String>();
map = (Map<String,String>) gson.fromJson(json, map.getClass());
```

3. String형식에 json을 DTO(VO)으로 변환

```Java
Gson gson = new Gson();
UserDTO user = new User();
user = gson.fromJson(json, UserDTO .class);
```


### 출처

- 출처: http://gompangyi.tistory.com/88 [곰팡이 월드]
- 출처: https://ko.wikipedia.org/wiki/JSON
- 출처: http://backback.tistory.com/167 [Back Ground]

## TODO

- glide라이브러리 옵션들 한번씩 사용해보거나, 필요할때 [glideGithub](https://github.com/bumptech/glide) 참고
- JSON데이터 형식 익히기(앞으로 많이 사용하므로)

## Retrospect

- 만약 이러한 라이브러리가 없을때는 어떻게 코드를 짜야할지 고민해보고, 직접 짜보기.
- 라이브러리를 향후에도 많이 사용할 것이기는 하지만, 그 원리를 파악하는 것도 중요함.

## Output
- 생략

