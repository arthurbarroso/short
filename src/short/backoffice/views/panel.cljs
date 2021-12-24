(ns short.backoffice.views.panel
  (:require [short.backoffice.template :as template]
            [reagent-table.core :as rt]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]))

(def columns [{:path [:product/title]
               :header "Title"
               :key :title}
              {:path [:product/price]
               :header "Price"
               :key :price}])

(defn- row-key-fn
  [row row-num]
  (str (get-in row [:product/title]) row-num))

(defn- cell-data
  [row cell]
  (let [{:keys [path expr]} cell]
    (or (and path
             (get-in row path))
        (and expr
             (expr row)))))

(defn- cell-fn
  [render-info row row-num col-num]
  (let [{:keys [format attrs]
         :or   {format identity
                attrs (fn [_] {})}} render-info
        data    (cell-data row render-info)
        content (format data)
        attrs   (attrs data)]
    [:span
     attrs
     content]))

(def table-state (atom {:draggable true}))

(defn panel-view []
  (let [products (re-frame/subscribe [::subs/products])]
    (cljs.pprint/pprint @products)
    (fn []
      [template/layout
       ^{:panel "login"}
       [:<>
        [:h2
         "Authenticated"]
        [rt/reagent-table @products
         {:table-state table-state
          :row-key row-key-fn
          :column-model columns
          :render-cell cell-fn
          :scroll-height "80vh"}]]])))
