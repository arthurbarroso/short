(ns short.shared.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfa]
            [short.shared.cookies :as cookies]
            [short.shared.session-storage :as session-storage]
            [short.shared.ui.toast :as toast]))

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
            :edit-product-form {:title nil
                                :slug nil
                                :sku nil
                                :price 1
                                :uuid nil}
            :variant-form {:type nil
                           :quantity 1
                           :image-url nil
                           :product-id nil}}}))

(re-frame/reg-fx
 ::toast
 (fn [{:keys [content]}]
   (toast/show-toast content)))

(re-frame/reg-fx
 ::success-toast
 (fn [{:keys [content]}]
   (toast/success-toast content)))

(re-frame/reg-fx
 ::failure-toast
 (fn [{:keys [content]}]
   (toast/failure-toast content)))

(re-frame/reg-fx
 ::warn-toast
 (fn [{:keys [content]}]
   (toast/warn-toast content)))

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

(re-frame/reg-event-fx
 ::http-failure
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)
    ::failure-toast {:content "failure"}}))

(def HttpRequestSchema
  [:map
   [:method keyword?]
   [:uri string?]
   [:params map?]
   [:authenticated? [:maybe boolean?]]
   [:on-success keyword?]
   [:on-failure keyword?]
   [:db :any]])

(defn build-http-request
  {:malli/schema [:=> [:cat HttpRequestSchema] :map]}
  [{:keys [method uri params
           authenticated? on-success on-failure
           db]}]
  {:db (assoc db :loading true)
   :http-xhrio {:method method
                :uri uri
                :format (ajax/json-request-format)
                :timeout 8000
                :params params
                :with-credentials (boolean authenticated?)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success on-success
                :on-failure on-failure}})
