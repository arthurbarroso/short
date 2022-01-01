(ns short.backoffice.views.panel
  (:require [short.backoffice.template :as template]
            [reagent-table.core :as rt]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.ui.text :as text]))

(def columns [{:path [:product/title]
               :header "Title"
               :key :title}
              {:path [:product/price]
               :header "Price"
               :key :price}
              {:path [:product/sku]
               :header "SKU"
               :key :sku}
              {:path [:product/slug]
               :header "Slug"
               :key :slug}
              {:path [:product/active]
               :header "Active"
               :format #(str %)
               :key :active}
              {:path [:product/created_at]
               :header "Created at"
               :key :created_at}
              {:path [:product/variant]
               :header "Variant count"
               :format #(count %)
               :key :variant
               :attrs (fn [_data] {:style {:text-align "center" :display "block"}})}])

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
    (fn []
      [template/layout
       ^{:key "panel"}
       [:<>
        [text/typography
         {:text "Product list"
          :variant "h2"}]
        [rt/reagent-table products
         {:table-state table-state
          :row-key row-key-fn
          :column-model columns
          :render-cell cell-fn
          :scroll-height "80vh"}]]])))
