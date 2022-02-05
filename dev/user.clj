(ns user
  (:require [short.server]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl]
            [integrant.repl.state :as state]
            [aero.core :refer [read-config]]
            [clojure.java.io :as io]
            [datahike.api :as d]
            [short.products.db :as products-db]
            [short.products.handlers :as products-handlers]
            [short.shared :as shared]))

(def environment-vars
  (try (read-config (io/resource "config.edn") {:profile :dev})
       (catch Exception e
         (clojure.pprint/pprint e))))

(def config-map
  {:server/jetty {:handler (ig/ref :short/app)
                  :port (:port environment-vars)}
   :short/app {:database (ig/ref :db/postgres)
               :auth {:jwt-secret (:jwt_secret environment-vars)}
               :s3 {:bucket (:s3_bucket environment-vars)
                    :creds {:access-key (:access_key environment-vars)
                            :endpoint (:aws_endpoint environment-vars)
                            :secret-key (:secret_key environment-vars)}}}
   :db/postgres {:host (:database_host environment-vars)
                 :port (:database_port environment-vars)
                 :user (:database_user environment-vars)
                 :backend (:database_backend environment-vars)
                 :id (:database_id environment-vars)
                 :password (:database_password environment-vars)
                 :dbname (:database_name environment-vars)}})

(ig-repl/set-prep!
 (fn [] config-map))

(def db (-> state/system :db/postgres))
(def app (-> state/system :short/app))
(def go ig-repl/go)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)
(def stop ig-repl/halt)

(comment
  (require '[malli.dev :as dev])
  (require '[malli.dev.pretty :as pretty])
  (dev/start! {:report (pretty/reporter)})
  (reset-all)
  (stop)
  (go)
  (println environment-vars)
  (comment
    (let [p (-> db
                products-db/list-products!
                flatten
                first)
          p-uuid (:product/uuid p)]
      (clojure.pprint/pprint p))
    (products-db/update-product! (java.util.UUID/fromString "e47994b2-82f6-473d-a357-4ee6409cdc8e")
                                 {:key :product/title
                                  :val "test"
                                  :key2 :product/sku
                                  :val2 "test"}
                                 db)
    (products-db/update-product!
     (java.util.UUID/fromString "e47994b2-82f6-473d-a357-4ee6409cdc8e")
     {:product/title "test-thing"}
     db)
    (products-db/get-product-by-uuid!
     (java.util.UUID/fromString "e47994b2-82f6-473d-a357-4ee6409cdc8e")
     db)
    (d/transact db
                [{:db/id [:product/uuid (java.util.UUID/fromString "e47994b2-82f6-473d-a357-4ee6409cdc8e")]
                  :product/sku "test-thing"
                  :product/title "test-thing"}])
    (let [r (products-handlers/update-product! {:sku "testando pae3" :title "novo2"}
                                             (java.util.UUID/fromString "e47994b2-82f6-473d-a357-4ee6409cdc8e")
                                             db)])))


      ;; (d/transact db [[:db/add [:product/uuid p-uuid] :product/sku "test" :product/title "test"]]))))
