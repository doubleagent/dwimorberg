(ns dwimorberg.impls.double-ops
  (:require [dwimorberg.proto :refer :all])
  (:import [clojure.lang Ratio BigInt]
           [java.math BigInteger BigDecimal RoundingMode MathContext]
           [dwimorberg Common]))

;; (set! *warn-on-reflection* true)

(defrecord DoublexLongOps []
  ITwoArityOps
  (add [_ x y] (Common/add ^double x ^long y)))

(defrecord DoublexDoubleOps []
  ITwoArityOps
  (add [_ x y] (Common/add ^double x ^double y)))

(defrecord DoublexBigIntegerOps []
  ITwoArityOps
  (add [_ x y]
    (.add (BigDecimal/valueOf ^double x)
          (BigDecimal. ^BigInteger y))))

(defrecord DoublexBigIntOps []
  ITwoArityOps
  (add [_ x y]
    (if (nil? (.bipart ^BigInt y))
      (Common/add ^double x ^long (.lpart ^BigInt y))
      (.add (BigDecimal/valueOf ^double x)
            (BigDecimal. ^BigInteger y)))))

(defrecord DoublexRatioOps []
  ITwoArityOps
  (add [_ x y]
    (if (Common/isInt ^double x)
      (Ratio.
       (.add
        (numerator y)
        (.multiply
         (denominator y)
         (.toBigInteger (BigDecimal/valueOf ^double x))))
       (denominator y))
      (let [numer (BigDecimal. (numerator y))
            denom (BigDecimal. (denominator y))]
        (.add
         (.divide numer denom 32 RoundingMode/HALF_UP)
         (BigDecimal/valueOf ^double x))))))

(defrecord DoublexBigDecOps []
  ITwoArityOps
  (add [_ x y] (.add ^BigDecimal y (BigDecimal/valueOf ^double x))))