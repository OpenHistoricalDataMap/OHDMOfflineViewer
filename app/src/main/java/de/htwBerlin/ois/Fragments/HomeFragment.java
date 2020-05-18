package de.htwBerlin.ois.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.File;
import java.util.ArrayList;

import de.htwBerlin.ois.FileStructure.RecyclerAdapterLocalMaps;
import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.MainActivity.MainActivity;
import de.htwBerlin.ois.R;

/**
 * Represents the HOME Tab and thus, the starting point for the OHDM Offline Viewer application
 *
 * @author morelly_t1
 * @author WilliBoelke
 */
public class HomeFragment extends Fragment
{

    //------------Instance Variables------------

    /**
     * Log tag
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Set of the .map files ind the OHDM directory
     * Filled in {@link  #findMapFiles()}
     */
    private ArrayList<File> mapFiles;
    /**
     * The view
     */
    private View view;
    /**
     * The RecyclerAdapter
     */
    private RecyclerAdapterLocalMaps recyclerAdapter;
    /**
     * The RecyclerView
     */
    private RecyclerView localMapsRecyclerView;


    //------------Static Variables------------

    /**
     * Fragment ID used to identify the fragment
     * (for example by putting the ID into the Intent extra )
     */
    public static String ID = "Home";


    //------------Activity/Fragment Lifecycle------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //inflating the view
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mapFiles = findMapFiles();
        setupRecycler();
        setupSearchView();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    //------------Setup Views------------

    /**
     * Setup for the SearchView
     */
    private void setupSearchView()
    {
        SearchView searchView = view.findViewById(R.id.local_maps_sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Setup method for the RecyclerView
     */
    private void setupRecycler()
    {
        localMapsRecyclerView = view.findViewById(R.id.local_maps_recycler);

        if (!mapFiles.isEmpty())
        {
            //In case there are files in the local OHDM directory these two will be behind the recycler
            view.findViewById(R.id.content_card_home_info).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.ohdm_logo_iv).setVisibility(View.INVISIBLE);

            localMapsRecyclerView.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerAdapter = new RecyclerAdapterLocalMaps(this.getContext(), mapFiles, R.layout.download_recycler_item);

            //onClickListener to set the clicked file in the MapFileSingleton
            recyclerAdapter.setOnItemClickListener(new RecyclerAdapterLocalMaps.OnItemClickListener()
            {
                @Override
                public void onItemClick(int position)
                {
                    Log.i(TAG, "using " + mapFiles.get(position).getName() + " as mapfile");
                    MapFileSingleton mapFile = MapFileSingleton.getInstance();
                    mapFile.setFile(mapFiles.get(position));
                    recyclerAdapter.notifyDataSetChanged();
                }
            });

            //Putting everything together
            localMapsRecyclerView.setLayoutManager(recyclerLayoutManager);
            localMapsRecyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();
        }
    }


    //------------Others------------

    /**
     * Scans OHDM Directory for .map Files
     *
     * @return a set of .map files
     */
    private ArrayList<File> findMapFiles()
    {
        ArrayList<File> maps = new ArrayList<>();
        try
        {
            for (File mapFile : new File(MainActivity.MAP_FILE_PATH).listFiles())
            {
                if (mapFile.getName().endsWith(".map"))
                {
                    Log.i(TAG, "osmfile: " + mapFile.getName());
                    maps.add(mapFile);
                }
            }
        }
        catch (NullPointerException e)
        {
            Log.i(TAG, "No map files located.");
        }
        return maps;
    }

}
