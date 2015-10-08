package kline.micah.itsrainingduhitsoregon;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {

    private static final String LOG_TAG = GetData.class.getSimpleName();

    private String mUrl;
    private String mData;


    public GetData(String url) {
        mUrl = url;
    }

    public void reset() {
        mUrl = null;
        mData = null;
    }

    public String getData() {
        return mData;
    }


    public void execute() {

        DownloadData downloadData = new DownloadData();
        downloadData.execute(mUrl);
    }

    public class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String resultString) {
            mData = resultString;
            Log.v(LOG_TAG, mData);
            if (mData == null) {
                if (mUrl == null) {
                    Log.v(LOG_TAG, "url not Initialized");

                } else {
                    Log.v(LOG_TAG, "Failed to Download or Data is NULL");

                }
            } else {
                Log.v(LOG_TAG, "STATUS_OK");

            }

        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if (params == null) {
                return null;
            }

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();


            } catch (IOException ioe) {
                Log.e(LOG_TAG, "ERROR", ioe);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "ERROR Closing Stream", e);
                    }
                }
            }

        }
    }

}
