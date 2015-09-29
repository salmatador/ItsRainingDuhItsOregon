package kline.micah.itsrainingduhitsoregon;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by desktop on 9/28/2015.
 */
enum DownloadStatus {
    IDLE, PROCESSES, NOT_INITALIZED, FAILED_OR_EMPTY, OK
}

public class GetData {

    private static final String LOG_TAG = GetData.class.getSimpleName();

    private String mUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetData(String url) {
        mUrl = url;
        mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset() {
        mDownloadStatus = DownloadStatus.IDLE;
        mUrl = null;
        mData = null;
    }

    public String getData() {
        return mData;
    }

    public DownloadStatus getDownloadStatus() {
        return mDownloadStatus;
    }

    public void execute() {
        mDownloadStatus = DownloadStatus.PROCESSES;
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
                    mDownloadStatus = DownloadStatus.NOT_INITALIZED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                mDownloadStatus = DownloadStatus.OK;
            }
            Log.v(LOG_TAG, mDownloadStatus.toString());
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
                    buffer.append(line + "\n");
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
