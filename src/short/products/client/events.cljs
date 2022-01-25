(ns short.products.client.events
  (:require [re-frame.core :as re-frame]
            [short.backoffice.events :as common-events]))

(re-frame/reg-event-fx
 ::navigate-to-product-variant-creation
 (fn [{:keys [db]} [_ {:keys [product-id product-name]}]]
   {:db (update-in db [:forms :variant-form] assoc
                   :product-id product-id :product-name product-name)
    ::common-events/navigate! [:create-variant]}))

(re-frame/reg-event-fx
 ::get-products
 (fn [{:keys [db]} [_]]
   (common-events/build-http-request
    {:db db
     :method :get
     :uri "http://localhost:4000/v1/products"
     :params nil
     :authenticated? true
     :on-success [::product-list-success]
     :on-failure [::common-events/http-failure]})))

(re-frame/reg-event-fx
 ::product-list-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db
               :products response
               :loading false)}))

(re-frame/reg-event-db
 ::set-product-form-field-value
 (fn [db [_ field-path new-value]]
   (assoc-in db [:forms :product-form field-path] new-value)))

(re-frame/reg-event-fx
 ::create-product
 (fn [{:keys [db]} [_ data]]
   (common-events/build-http-request
    {:db db
     :method :post
     :uri "http://localhost:4000/v1/products"
     :params data
     :authenticated? true
     :on-success [::product-create-success]
     :on-failure [::common-events/http-failure]})))

(re-frame/reg-event-fx
 ::product-create-success
 (fn [{:keys [db]} [_ response]]
   (let [products-in-db (:products db)]
     {:db (assoc db
                 :loading false
                 :products (conj products-in-db response))})))
