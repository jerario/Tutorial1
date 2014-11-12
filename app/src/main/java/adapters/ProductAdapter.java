package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerario.tutorial1.R;
import com.example.jerario.tutorial1.entities.Item;

import java.util.LinkedList;

/**
 * Created by jerario on 11/11/14.
 */
public class ProductAdapter extends BaseAdapter{
    private LinkedList<Item> products;
    private LayoutInflater lInflater;


    public ProductAdapter(Context context, LinkedList<Item> products){
        this.lInflater = LayoutInflater.from(context);
        this.products = products;
    }

    @Override
    public int getCount() {
        if (products == null)
            return 0;
        else
            return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("PRODUCT ADAPTER","IN");
        ContenedorView contenedor;
        if (view == null) {
            contenedor = new ContenedorView();
            view = lInflater.inflate(R.layout.product_layout,null);
         //   contenedor.pic = (ImageView) view.findViewById(R.id.pic);
            contenedor.title = (TextView) view.findViewById(R.id.title);

            View subtitleView = view.findViewById(R.id.subtitle);
            if (subtitleView != null)
                contenedor.subtitle = (TextView) subtitleView;

            View priceView = view.findViewById(R.id.price);
            if (priceView != null)
                contenedor.price= (TextView) priceView;

            View quantityView = view.findViewById(R.id.quantity);
            if (quantityView != null)
                contenedor.available_quantity= (TextView) quantityView;

            view.setTag(contenedor);
        }
        else
            contenedor = (ContenedorView) view.getTag();

        Item item = (Item) getItem(i);
        contenedor.title.setText(item.getTitle());

        //Price
        contenedor.price.setText("$"+Double.toString(item.getPrice()));

        //Stock
        String stock = view.getContext().getString(R.string.stock);
        String quantity = Integer.toString(item.getAvailable_quantity());
        contenedor.available_quantity.setText(stock +":"+quantity);

        //Subtitle
        if (contenedor.subtitle != null)
            if (item.getSubtitle() != null)
                contenedor.subtitle.setText(item.getSubtitle());
            else
                contenedor.subtitle.setText("");
        //contenedor.pic.setImageDrawable(item.getPic());
 //       Log.d("PRODUCT ADAPTER FINISHING", item.getTitle());

        return view;
    }

    class ContenedorView{
        TextView title;
        TextView subtitle;
        TextView price;
        TextView available_quantity;
        //ImageView pic;
    }



}
