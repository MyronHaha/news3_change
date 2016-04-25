package com.example.whoami.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public  class Tool extends AsyncTask<String, Void, Bitmap>
{

    View v;
    String type ;
    Tool(View v,String type){
        this.v = v;
        this.type= type;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        if(type.equals("1")) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap drawable) {

        ImageView imageView = (ImageView) v.findViewById(R.id.image);
        imageView.setImageBitmap(drawable);

        super.onPostExecute(drawable);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
