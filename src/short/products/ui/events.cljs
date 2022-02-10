(ns short.products.ui.events
  (:require [re-frame.core :as re-frame]
            [short.shared.events :as common-events]))

(re-frame/reg-event-fx
 ::navigate-to-product-variant-creation
 (fn [{:keys [db]} [_ {:keys [product-id product-name]}]]
   {:db (update-in db [:forms :variant-form] assoc
                   :product-id product-id :product-name product-name)
    ::common-events/navigate! [:create-variant]}))

(re-frame/reg-event-db
 ::set-variant
 (fn [db [_ {:keys [product-id product-name]}]]
   (update-in db [:forms :variant-form] assoc
              :product-id product-id :product-name product-name)))

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

(re-frame/reg-event-db
 ::set-edit-product-form-field-value
 (fn [db [_ field-path new-value]]
   (assoc-in db [:forms :edit-product-form field-path] new-value)))

(re-frame/reg-event-db
 ::set-product-edit-form
 (fn [db [_ data]]
   (assoc-in db [:forms :edit-product-form] data)))

(re-frame/reg-event-fx
 ::edit-product
 (fn [{:keys [db]} [_ data]]
   (common-events/build-http-request
    {:db db
     :method :put
     :uri (str "http://localhost:4000/v1/products/update/" (:uuid data))
     :params data
     :authenticated? true
     :on-success [::product-edit-success]
     :on-failure [::common-events/http-failure]})))

(re-frame/reg-event-fx
 ::product-edit-success
 (fn [_ [_ _response]]
   {:dispatch [::get-products]}))
