# Json

## 특징
-JSON(JavaScript Object Notation)은 인터넷에서 자료를 주고받을 때 그 자료를 표현하는 방법
-자료의 종류에 큰 제한은 없으며, 특히 컴퓨터 프로그램의 변수값을 표현하는 데 적합

출처: http://gompangyi.tistory.com/88 [곰팡이 월드]

## Json을 통해 openApi데이터 받아오기
### Main
- library를 사용해봄
- Json,Gson

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
        List<User> result = Arrays.asList(user);
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

-Glide를 사용해봄

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
