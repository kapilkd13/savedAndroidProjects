package com.example.anurag.connect_net;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sarfraz on 28-05-2016.
 */
public class NewPostFragment1 extends Fragment {
    TextView tv_title, tv_category, tv_timestamp, tv_text;
    ImageView img_1,img_2,img_3;
    VideoView video;
    String title,category,timestamp,text,FindInDevice;
    int noImg,noVideo,id;
LinearLayout videoLayout;
    Button downloadVideo,playVideo;
    View view;
    public void addDetails(int id,String title, String category, String timestamp, String text, int noImg, int noVideo){
        this.id=id;
        this.title=title;
        this.category = category;
        this.timestamp = timestamp;
        this.text = text;
        this.noImg=noImg;
        this.noVideo=noVideo;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_post_fragment1, container,false);
        init();

        tv_text.setText(text);
        tv_title.setText(title);
        tv_category.setText(category);
        tv_timestamp.setText(timestamp);
        switch (noImg){
            case 3:

                FindInDevice=new DownloadedFileAddress().getDownloadedFileAddress(Constants.IMAGE_NAME3,Constants.TIMELINE,id,getContext());
               if(FindInDevice!=null) {

                   Picasso.with(getActivity())
                           .load(new File(FindInDevice))
                           .placeholder(R.drawable.placeholder)
                           .skipMemoryCache()

                           .resize(getScreenWidth(), 400)  //  .fit()
                           .into(img_3);
               }
                else
               {Log.v("going great","null");
                   Picasso.with(getActivity())
                       .load(makeWebURL(id,Constants.IMAGE_NAME3))
                       .placeholder(R.drawable.placeholder)
                       .skipMemoryCache()

                       .resize(getScreenWidth(), 400)  //  .fit()
                       .into(img_3);
               }
                Log.v("loading img", "loading");

            case 2:
                FindInDevice=new DownloadedFileAddress().getDownloadedFileAddress(Constants.IMAGE_NAME2,Constants.TIMELINE,id,getContext());
                if(FindInDevice!=null) {

                    Picasso.with(getActivity())
                            .load(new File(FindInDevice))
                            .placeholder(R.drawable.placeholder)
                            .skipMemoryCache()
                            .centerCrop()
                            .resize(getScreenWidth(), 400)  //  .fit()
                            .into(img_2);
                }
                else
                {Log.v("going great","null");
                    Picasso.with(getActivity())
                            .load(makeWebURL(id,Constants.IMAGE_NAME2))
                            .placeholder(R.drawable.placeholder)
                            .skipMemoryCache()
                            .centerCrop()
                            .resize(getScreenWidth(), 400)  //  .fit()
                            .into(img_2);
                }
                Log.v("loading img2222", "loading");

            case 1:
                FindInDevice=new DownloadedFileAddress().getDownloadedFileAddress(Constants.IMAGE_NAME1,Constants.TIMELINE,id,getContext());
                if(FindInDevice!=null) {

                    Picasso.with(getActivity())
                            .load(new File(FindInDevice))
                            .placeholder(R.drawable.placeholder)
                            .skipMemoryCache()
                            .centerCrop()
                            .resize(getScreenWidth(), 400)  //  .fit()
                            .into(img_1);
                }
                else
                {Log.v("going great","null");
                  Picasso.with(getActivity())
                            .load(makeWebURL(id,Constants.IMAGE_NAME1))
                            .placeholder(R.drawable.placeholder)
                            .skipMemoryCache()
                            .centerCrop()
                            .resize(getScreenWidth(), 400)  //  .fit()
                            .into(img_1);
                }
                Log.v("loading img3333", "loading");
            case 0:
                break;
            default:
                Log.d("image load", "other than 1,2,3,0 occured");
                break;
        }

        switch(noVideo)
        {
            case 1:
                //set layout visible
                videoLayout.setVisibility(view.VISIBLE);
             //check if we can create thumbnail or we will use default placeholder

                    createThumbnail();
                //inflate videoview
                playVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getContext(), com.example.anurag.connect_net.playVideo.class);
                        i.putExtra("category",Constants.TIMELINE);
                        i.putExtra("id",id);//change to dynamic id
                        i.putExtra("url",Constants.SERVER_TIMELINE_DIRECTORY_PATH);
                        i.putExtra("filename",Constants.VIDEO_NAME);//change to dynamic file name
                        startActivity(i);

                    }
                });
                downloadVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> videolist=new ArrayList<>();
                        videolist.add(Constants.SERVER_TIMELINE_DIRECTORY_PATH);
                        videolist.add(Integer.toString(id));
//default video name is vid1.mp4
                        videolist.add(Constants.VIDEO_NAME);//provide dynamic video name
                        videolist.add(new DownloadedFileAddress().getNewFileAddress(Constants.VIDEO_NAME, Constants.TIMELINE, id, getContext()));
                        //do this in service
                        //     new UploadFileToServer().execute(videolist);
                        Intent i=new Intent(getContext(), com.example.anurag.connect_net.DownloadVideoInService.class);
                        i.putStringArrayListExtra("data", videolist);
                        getContext().startService(i);
                    }
                });

        }


        return view;
    }
    private int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }
    private String makeWebURL(int id,String filename)
    {
        return (Constants.SERVER_TIMELINE_DIRECTORY_PATH+Constants.PATH_DELIMETER+id+Constants.PATH_DELIMETER+filename);
    }
    public void init(){
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_category = (TextView) view.findViewById(R.id.tv_category);
        tv_timestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        img_1=(ImageView)view.findViewById(R.id.img1);
        img_2=(ImageView)view.findViewById(R.id.img2);
        img_3=(ImageView)view.findViewById(R.id.img3);
        video=(VideoView) view.findViewById(R.id.video_view);

        videoLayout=(LinearLayout)view.findViewById(R.id.video_layout);
        downloadVideo=(Button)view.findViewById(R.id.downloadButton);
        playVideo=(Button)view.findViewById(R.id.playButton);
    }

    private void createThumbnail()
    {Bitmap thumb;
        String localaddress=new DownloadedFileAddress().getDownloadedFileAddress(Constants.VIDEO_NAME, Constants.TIMELINE, id, getContext());
        if(localaddress!=null)
        {
         thumb= ThumbnailUtils.createVideoThumbnail(localaddress,MediaStore.Images.Thumbnails.MINI_KIND);
    }
        else
            thumb= BitmapFactory.decodeResource(getContext().getResources(),R.drawable.videoplaceholder);

        video.setBackground(new BitmapDrawable(thumb));
    }
}
