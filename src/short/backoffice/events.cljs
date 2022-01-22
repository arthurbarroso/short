(ns short.backoffice.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfa]
            [short.cookies :as cookies]
            [short.session-storage :as session-storage]
            [clojure.string :as string]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_]
   {:email nil
    :password nil
    :authenticated? false
    :token nil
    :current-route nil
    :products []
    :forms {:product-form {:title nil
                           :slug nil
                           :sku nil
                           :price 1}
            :variant-form {:type nil
                           :quantity 1
                           :image-url nil
                           :product-id nil}}}))

(re-frame/reg-fx
 ::set-session-storage!
 (fn [{:keys [key value]}]
   (session-storage/set! {:key key
                          :data value})))

(re-frame/reg-cofx
 ::session-storage
 (fn [coeffects session-storage-key]
   (assoc coeffects
          ::session-storage (session-storage/get! session-storage-key))))

(re-frame/reg-event-fx
 ::load-from-session-storage
 [(re-frame/inject-cofx ::session-storage "authenticated?")]
 (fn [cofx _]
   (let [val (::session-storage cofx)
         db (:db cofx)]
     {:db (assoc db :authenticated? val)})))

(re-frame/reg-fx
 ::set-cookie!
 (fn [{:keys [key value]}]
   (cookies/set-raw-cookie! key value)))

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ route params query]]
   {::navigate! [route params query]}))

(re-frame/reg-fx
 ::navigate!
 (fn [[route params query]]
   (rfa/push-state route params query)))

(re-frame/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(re-frame/reg-event-db
 ::change-email-input
 (fn [db [_ new-val]]
   (assoc db :email new-val)))

(re-frame/reg-event-db
 ::change-password-input
 (fn [db [_ new-val]]
   (assoc db :password new-val)))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} [_ credentials]]
   {:db (assoc db :loading true)
    :http-xhrio {:method :post
                 :uri (str "http://localhost:4000/v1/users/login")
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params credentials
                 :with-credentials true
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::login-success]
                 :on-failure [::login-failure]}}))

(re-frame/reg-event-fx
 ::login-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db
               :loading false
               :token (:token response)
               :authenticated? true)
    ::set-cookie! {:key "token"
                   :value (:token response)}
    ::set-session-storage! {:key "authenticated?"
                            :value true}
    ::navigate! [:panel]}))

(re-frame/reg-event-fx
 ::login-failure
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)}))

(re-frame/reg-event-fx
 ::get-products
 (fn [{:keys [db]} [_ credentials]]
   {:db (assoc db :loading true)
    :http-xhrio {:method :get
                 :uri (str "http://localhost:4000/v1/products")
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params credentials
                 :with-credentials true
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::product-list-success]
                 :on-failure [::product-list-failure]}}))

(re-frame/reg-event-fx
 ::product-list-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db
               :products response)}))

(re-frame/reg-event-fx
 ::product-list-failure
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db :loading false)}))

(re-frame/reg-event-db
 ::set-product-form-field-value
 (fn [db [_ field-path new-value]]
   (assoc-in db [:forms :product-form field-path] new-value)))

(re-frame/reg-event-fx
 ::create-product
 (fn [{:keys [db]} [_ data]]
   {:db (assoc db :loading true)
    :http-xhrio {:method :post
                 :uri (str "http://localhost:4000/v1/products")
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params data
                 :with-credentials true
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::product-create-success]
                 :on-failure [::product-create-failure]}}))

(re-frame/reg-event-fx
 ::product-create-success
 (fn [{:keys [db]} [_ response]]
   (let [products-in-db (:products db)]
     {:db (assoc db
                 :loading false
                 :products (conj products-in-db response))})))

(re-frame/reg-event-fx
 ::product-create-failure
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)}))

(re-frame/reg-event-db
 ::set-variant-form-field-value
 (fn [db [_ field-path new-value]]
   (assoc-in db [:forms :variant-form field-path] new-value)))

(re-frame/reg-event-fx
 ::navigate-to-product-variant-creation
 (fn [{:keys [db]} [_ {:keys [product-id product-name]}]]
   {:db (update-in db [:forms :variant-form] assoc
                   :product-id product-id :product-name product-name)
    ::navigate! [:create-variant]}))

(re-frame/reg-event-fx
 ::create-product-variant
 (fn [{:keys [db]} [_ form-data]]
   (let [{:keys [file-type file-key file-size]} form-data]
     {:db (assoc db :loading true)
      :http-xhrio {:method :post
                   :uri "http://localhost:4000/v1/s3/generate"
                   :format (ajax/json-request-format)
                   :timeout 8000
                   :params {:file-type file-type
                            :file-key file-key
                            :file-size (js/parseFloat file-size)}
                   :with-credentials true
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [::s3-url-success form-data]
                   :on-failure [::s3-url-failure]}})))

(re-frame/reg-event-fx
 ::s3-url-failure
 (fn [{:keys [db]} [_ _response]]
   {:db (assoc db :loading false)}))

(re-frame/reg-event-fx
 ::s3-url-success
 (fn [{:keys [db]} [_ form-data response]]
   (let [s3-url (:s3/url response)
         params-index (string/index-of s3-url "?")
         file-url (subs s3-url 0 params-index)]
     {:db (assoc db :loading false)
      :dispatch [::upload-image (merge response form-data {:file-url file-url})]})))

(re-frame/reg-event-fx
 ::upload-image
 (fn [{:keys [db]} [_ data]]
   {:db (assoc db :loading true)
    :http-xhrio {:method :put
                 :uri (:s3/url data)
                 :timeout 8000
                 :body (.get (:file data) "file")
                 :headers {"Content-Type" "image/*"}
                 :response-format (ajax/raw-response-format)
                 :on-success [::upload-image-success data]
                 :on-failure [::s3-url-failure]}}))

(re-frame/reg-event-fx
 ::upload-image-success
 (fn [{:keys [db]} [_ data _response]]
   (let [uploaded-file-url (:file-url data)]
     {:db (assoc db :loading false)
      :dispatch [::create-variant (assoc data
                                         :image-url
                                         uploaded-file-url)]})))

(re-frame/reg-event-fx
 ::create-variant
 (fn [{:keys [db]} [_ data]]
   {:db (assoc db :loading true)
    :http-xhrio {:method :post
                 :uri (str "http://localhost:4000/v1/variants/" (:product-id data))
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params (assoc data :active true)
                 :with-credentials true
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::variant-create-success]
                 :on-failure [::variant-create-failure]}}))

(re-frame/reg-event-fx
 ::variant-create-success
 (fn [{:keys [db]} [_ _response]]
   {:db (assoc db
               :loading false)
    ::navigate! [:panel]}))

(re-frame/reg-event-fx
 ::variant-create-failure
 (fn [{:keys [db]} [_ _response]]
   {:db (assoc db :loading false)}))
