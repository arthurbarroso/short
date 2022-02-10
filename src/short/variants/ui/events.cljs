(ns short.variants.ui.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [clojure.string :as string]
            [short.shared.events :as common-events]))

(re-frame/reg-event-db
 ::set-variant-form-field-value
 (fn [db [_ field-path new-value]]
   (assoc-in db [:forms :variant-form field-path] new-value)))

(re-frame/reg-event-fx
 ::create-product-variant
 (fn [{:keys [db]} [_ form-data]]
   (let [{:keys [file-type file-key file-size]} form-data]
     (common-events/build-http-request
      {:db db
       :method :post
       :uri "http://localhost:4000/v1/s3/generate"
       :params {:file-type file-type
                :file-key file-key
                :file-size (js/parseFloat file-size)}
       :authenticated? true
       :on-success [::s3-url-success form-data]
       :on-failure ::common-events/http-failure}))))

(re-frame/reg-event-fx
 ::s3-url-failure
 (fn [{:keys [db]} [_ _response]]
   {:db (assoc db :loading false)}))

(re-frame/reg-event-fx
 ::s3-url-success
 (fn [_ [_ form-data response]]
   (let [s3-url (:s3/url response)
         params-index (string/index-of s3-url "?")
         file-url (subs s3-url 0 params-index)]
     {:dispatch [::upload-image (merge response form-data {:file-url file-url})]})))

(re-frame/reg-event-fx
 ::upload-image
 (fn [_ [_ data]]
   {:http-xhrio {:method :put
                 :uri (:s3/url data)
                 :timeout 8000
                 :body (.get (:file data) "file")
                 :headers {"Content-Type" "image/*"}
                 :response-format (ajax/raw-response-format)
                 :on-success [::upload-image-success data]
                 :on-failure [::s3-url-failure]}}))

(re-frame/reg-event-fx
 ::upload-image-success
 (fn [_ [_ data _response]]
   (let [uploaded-file-url (:file-url data)]
     {:dispatch [::create-variant (assoc data
                                         :image-url
                                         uploaded-file-url)]})))

(re-frame/reg-event-fx
 ::create-variant
 (fn [{:keys [db]} [_ data]]
   (common-events/build-http-request
    {:method :post
     :db db
     :uri (str "http://localhost:4000/v1/variants/" (:product-id data))
     :params (assoc data :active true)
     :authenticated? true
     :on-success [::variant-create-success]
     :on-failure [::variant-create-failure]})))

(re-frame/reg-event-fx
 ::variant-create-success
 (fn [{:keys [db]} [_ _response]]
   {:db (-> db
            (update :loading not)
            (update-in [:forms :variant-form]
                       (fn [_x]
                         {:type nil
                          :quantity 1
                          :image-url nil
                          :product-id nil})))
    ::common-events/navigate! [:panel]}))

(re-frame/reg-event-fx
 ::variant-create-failure
 (fn [{:keys [db]} [_ _response]]
   {:db (assoc db :loading false)}))

(re-frame/reg-event-fx
 ::edit-variant
 (fn [{:keys [db]} [_ data]]
   (common-events/build-http-request
    {:db db
     :method :put
     :uri (str "http://localhost:4000/v1/variants/update/" (:uuid data))
     :params data
     :authenticated? true
     :on-success [::variant-edit-success]
     :on-failure [::common-events/http-failure]})))

(re-frame/reg-event-fx
 ::variant-edit-success
 (fn [_ [_ _response]]
   {}))
