package com.sjain.snapdealsearchbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;

import com.sjain.snapdealsearchbar.adapter.ProductListAdapter;
import com.sjain.snapdealsearchbar.model.ProductDescription;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //View
    @BindView(R.id.rv_product_list)
    RecyclerView rvProductList;
    @BindView(R.id.sv_search_view)
    SearchView svSearchView;
    @BindView(R.id.ll_no_internet_connection)
    LinearLayout llNoInternetConnection;
    @BindView(R.id.ll_progress_bar)
    LinearLayout llProgressbar;
    @BindView(R.id.ll_no_result)
    LinearLayout llNoResult;

    public static String RUPEE_WITH_SPACE = "₹ ";

    private RecyclerView.LayoutManager mLayoutManager;
    Context mContext;
    private ProductListAdapter mProductListAdapter;
    ArrayList<ProductDescription> mProductDescriptionList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        svSearchView.setQueryHint("Enter Keyword");
        svSearchView.setQuery("mobile", true);
        if (isNetworkAvailable()) {
            new ExecuteKeywordSearch().execute("mobile");
        } else {
            llNoInternetConnection.setVisibility(View.VISIBLE);
            llProgressbar.setVisibility(View.GONE);
            llNoResult.setVisibility(View.GONE);
            rvProductList.setVisibility(View.GONE);
        }

        mLayoutManager = new GridLayoutManager(mContext, 2);

        rvProductList.setLayoutManager(mLayoutManager);

        svSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isNetworkAvailable()) {
                    new ExecuteKeywordSearch().execute(query);
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llProgressbar.setVisibility(View.GONE);
                    llNoResult.setVisibility(View.GONE);
                    rvProductList.setVisibility(View.GONE);
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    //OnBackPressed Method
    @Override
    public void onBackPressed() {
        if (!svSearchView.isIconified()) {
            svSearchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    //Load the reuslt in RecyclerView
    public void loadProductInView() {
        mProductListAdapter = new ProductListAdapter(mContext, mProductDescriptionList);
        rvProductList.setAdapter(mProductListAdapter);
    }

    //Async task for query
    private class ExecuteKeywordSearch extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llNoInternetConnection.setVisibility(View.GONE);
            llProgressbar.setVisibility(View.VISIBLE);
            llNoResult.setVisibility(View.GONE);
            rvProductList.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.snapdeal.com/search?keyword=" + strings[0] + "&sort=rlvncy").get();
                Elements linkImage = doc.select("source[srcset]");
                Elements productPrice = doc.select("span[display-price]");

                mProductDescriptionList = new ArrayList<ProductDescription>();

                if (linkImage != null && productPrice != null) {

                    for (int i = 0; i < linkImage.size(); i++) {

                        ProductDescription mProductDescription = new ProductDescription();
                        mProductDescription.setTitle(linkImage.get(i).attr("title"));
                        mProductDescription.setImageSrc(linkImage.get(i).attr("srcset"));
                        mProductDescription.setPrice(productPrice.get(i).attr("display-price"));
                        mProductDescriptionList.add(mProductDescription);
                    }
                } else {
                    mProductDescriptionList = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mProductDescriptionList != null && mProductDescriptionList.size() > 0) {
                loadProductInView();
                llNoInternetConnection.setVisibility(View.GONE);
                llProgressbar.setVisibility(View.GONE);
                llNoResult.setVisibility(View.GONE);
                rvProductList.setVisibility(View.VISIBLE);
            } else {
                llNoInternetConnection.setVisibility(View.GONE);
                llProgressbar.setVisibility(View.GONE);
                llNoResult.setVisibility(View.VISIBLE);
                rvProductList.setVisibility(View.GONE);
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


}
