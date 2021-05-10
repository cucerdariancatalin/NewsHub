package com.example.newsforyou.Fragments

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsforyou.Activities.MainActivity
import com.example.newsforyou.Adapters.NewsAdapter
import com.example.newsforyou.Model.NewsHeadlines
import com.example.newsforyou.Model.NewsModel
import com.example.newsforyou.Network.RetrofitClient
import com.example.newsforyou.Network.apiInterface
import com.example.newsforyou.R
import com.example.newsforyou.database.Bookmark
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*


class NewsFragment : Fragment() {

    lateinit var newsRecyclerView: RecyclerView
    private lateinit var drawer_button: ImageView
    lateinit var skeleton: Skeleton
    lateinit var newsHeading: TextView


    val response: MutableLiveData<Response<NewsModel>> = MutableLiveData()


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val a = view.context as MainActivity
        a.motion_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        drawer_button = view.findViewById(R.id.drawer_button)
        newsHeading = view.findViewById(R.id.news_heading)
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        shimmerEffect()

        val apiInterface: apiInterface = RetrofitClient.retrofit.create(apiInterface::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                response.postValue(apiInterface.getNews())
                Toast.makeText(view.context, "News Updated", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("NewsError", e.message!!)
            }
        }
        fetchNews(view)


        a.entertainment.setOnClickListener {
            newsHeading.text = "Entertainment"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getEntertainmentNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.business.setOnClickListener {
            newsHeading.text = "Business"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getBusinessNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.sports.setOnClickListener {
            newsHeading.text = "Sports"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getSportsNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.medical.setOnClickListener {
            newsHeading.text = "Medical"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getMedicalNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.international.setOnClickListener {
            newsHeading.text = "International"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getInternationalNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.technology.setOnClickListener {
            newsHeading.text = "Technology"
            a.motion_layout.closeDrawer(Gravity.START, true)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    response.postValue(apiInterface.getTechnologyNews())
                    Toast.makeText(view.context, "Api fetch", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("NewsError", e.message!!)
                }
            }
            fetchNews(view)
        }

        a.bookmarkTab.setOnClickListener {
            newsHeading.text = "Bookmark"
            a.motion_layout.closeDrawer(Gravity.START, true)

            Bookmark(view.context).bookmarkDao().getBookmarks().observe(this, Observer {

                val myList = mutableListOf<NewsHeadlines>()
                for (i in it)
                    if (!i.urlToImage.isNullOrEmpty()) {

                        myList.add(
                            NewsHeadlines(
                                i.author,
                                i.id.toString(),
                                i.name,
                                i.title,
                                i.description,
                                i.url,
                                i.urlToImage,
                                i.publishedAt,
                                i.content
                            )
                        )

                    }

                myList.reverse()
                if (myList.isNullOrEmpty())
                    Toast.makeText(view.context, "No items to visible", Toast.LENGTH_SHORT).show()

                newsRecyclerView.adapter = NewsAdapter(view.context, myList)
            })
        }


        drawer_button.setOnClickListener {
            val a = (it.context as MainActivity)
            a.motion_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            a.motion_layout.openDrawer(GravityCompat.START)
        }

        return view
    }

    @SuppressLint("ResourceType")
    private fun shimmerEffect() {
        skeleton = newsRecyclerView.applySkeleton(R.layout.shimmer_layout, 10)
        skeleton.maskCornerRadius = 30F
        skeleton.shimmerDurationInMillis = 2000
        skeleton.showShimmer = true
        skeleton.showSkeleton()
    }

    private fun fetchNews(view: View) {
        response.observe(this, Observer {
            val newList = ArrayList<NewsHeadlines>()
            for (i in it.body()?.articles!!)
                if (!i.urlToImage.isNullOrEmpty()) {
                    newList.add(
                        NewsHeadlines(
                            i.author,
                            i.source.id,
                            i.source.name,
                            i.title,
                            i.description,
                            i.url,
                            i.urlToImage,
                            i.publishedAt,
                            i.content
                        )
                    )
                }

//            Handler().postDelayed(shimmerEffect(),1000)
            newsRecyclerView.adapter = NewsAdapter(view.context, newList)
        })
    }
}