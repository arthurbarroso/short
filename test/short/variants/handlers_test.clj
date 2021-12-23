(ns short.variants.handlers-test
  (:require [short.variants.handlers :as h]
            [short.products.handlers :as ph]
            [test-helpers :as th]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.variants.contracts :as c]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "variants-tests-handlers"))
    (f)))

(defn p [opts]
  (merge
   {:product-id :to-be-inserted
    :active true
    :quantity 30
    :type "gg"
    :image-url "image-url"}
   opts))

(deftest variants-create-variant-handler-test
  (testing "Creates a new variant from external data and prepares
            it to be sent in the output"
    (let [database database-conn
          product (ph/create-product!
                   {:sku "fasjfsaoij123331"
                    :active true
                    :slug "some-slug31"
                    :title "some-title31"
                    :price 30} @database)
          result (h/create-variant! (p
                                     {:product-id
                                      (:product/uuid product)})
                                    @database)]
      (is (true? (ml/validate c/VariantOut result))))))
