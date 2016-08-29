package com.example.anurag.connect_net;

/**
 * Created by Anurag on 26-05-2016.
 */


        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.SocketTimeoutException;
        import java.net.URI;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.widget.TextView;

public class PhpRequest  extends AsyncTask<String,Void,String>{


    private int byGetOrPost = 0;
    SharedPreferences.Editor EPref;
    SharedPreferences sp;
    String type;
    TextView show;
    String category;
    Context context;
    private String topic;
    Fragment f;
    int fragtype;
private  boolean callback=false;
    //flag 0 means get and 1 means post.(By default it is get.)
    public PhpRequest(SharedPreferences sp,SharedPreferences.Editor EPref,TextView show,int flag) {
        this.EPref=EPref;
        this.sp=sp;
        this.show=show;
        byGetOrPost = flag;
    }
    public PhpRequest(SharedPreferences sp,SharedPreferences.Editor EPref,int flag) {
        this.EPref=EPref;
        this.sp=sp;
        byGetOrPost = flag;
    }
    public PhpRequest(Context context,int flag) {

        this.context=context;
        byGetOrPost = flag;

    }

    public PhpRequest(Context context,Fragment f,int fragtype,int flag) {
this.f=f;
        this.fragtype=fragtype;
        this.context=context;
        byGetOrPost = flag;
        callback=true;
    }
public PhpRequest(Timeline t)
{}


    @Override
    protected String doInBackground(String... arg0) {
        if(byGetOrPost == 0){ //means by Get Method

            try{
                String username = (String)arg0[0];
                String password = (String)arg0[1];
                String link = "http://myphpmysqlweb.hostei.com/login.php?username="+username+"& password="+password;

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        else if(byGetOrPost == 1){
            try{
                 type = (String)arg0[0];
                String link=(String)arg0[1];
                String data  = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
               // data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }else if(byGetOrPost == 2)  {
            try{
                type = (String)arg0[0];
                category=(String)arg0[1];
                String id=(String)arg0[2];
                String link=(String)arg0[3];
                String data  = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                 data += "&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
                data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch (SocketTimeoutException e)
            {
                Log.v("socket is timed  out", "whaat next");
                e.printStackTrace();
                return  "socked was timed out";}

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }else   {
            try{
                type = (String)arg0[0];
                category=(String)arg0[1];
                topic=(String)arg0[2];
                String link=(String)arg0[3];
                String data  = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                data += "&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
                data += "&" + URLEncoder.encode("topic", "UTF-8") + "=" + URLEncoder.encode(topic, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch (SocketTimeoutException e)
            {
                Log.v("socket is timed  out", "whaat next");
                e.printStackTrace();
                return  "socked was timed out";}
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }
    }

    @Override
    protected void onPostExecute(String result){
        if(byGetOrPost==1) {
            switch (type){
                case "connection":
                    if(!(sp.getString(Constants.SHARED_PREFERENCE_CONNECTION_KEY,"no").equals("yes")&&result.toString().equals("yes"))) {
                        EPref.putString(Constants.SHARED_PREFERENCE_CONNECTION_KEY, result);
                        EPref.commit();
                    }
                    break;
                case "jsoncheck":
                    String data = "";
                    JSONObject jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            String title = jsonObject.optString("title").toString();
                            String url =jsonObject.optString("url").toString();
                            data += "Node"+i+" : \n title= "+ title +" \n url = "+ url +" \n ";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    this.show.setText(data);
                    break;
                case "timeline":
                     data = "";
                     jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                         InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_TIMELINE_FILE,jsonArray.toString());

                       // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "faqs":
                    data = "";
                    jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                        InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_FAQ_FILE,jsonArray.toString());

                        // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "learn_category":
                    data = "";
                    jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                        InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_LEARN_CATEGORY_FILE,jsonArray.toString());

                        // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "poll":
                    data = "";
                    jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                        InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_POLL_FILE,jsonArray.toString());

                        // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }else if(byGetOrPost==2){
            String data = "";
            JSONObject jsonRootObject = null;
            data = "";
            jsonRootObject = null;
            try {
                jsonRootObject = new JSONObject(result);
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                //jsonRootObject =new JSONObject(jsonArray.toString());
                InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_CATEGORY_FILE+category,jsonArray.toString());

                // this.show.setText(jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(byGetOrPost==3){
            switch (type){
                case "topic":
                    String data = "";
                    JSONObject jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                        InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_LEARN_TOPIC_FILE+category,jsonArray.toString());

                        // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "content":
                     data = "";
                     jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(result);
                        //Get the instance of JSONArray that contains JSONObjects
                        JSONArray jsonArray = jsonRootObject.optJSONArray("posts");
                        //jsonRootObject =new JSONObject(jsonArray.toString());
                        InternalStorage.write(this.context,Constants.INTERNAL_STORAGE_LEARN_TOPIC_CONTENT_FILE+category+topic,jsonArray.toString());

                        // this.show.setText(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }
if(callback){
        if(fragtype==1)
        {((Timeline)f).afterphpCall();}

       else if(fragtype==2)
        {((Learning)f).afterphpCall(0);}

      else  if(fragtype==3)
        {((Polls)f).afterphpCall();}

       else if(fragtype==4)
        {((Faqs)f).afterphpCall(0);}
    }}
}