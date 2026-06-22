ALTER TABLE reviews
DROP CONSTRAINT reviews_rating_overall_check,
DROP CONSTRAINT reviews_rating_quality_check,
DROP CONSTRAINT reviews_rating_punctuality_check,
DROP CONSTRAINT reviews_rating_communication_check,
DROP CONSTRAINT reviews_rating_price_fairness_check,
DROP CONSTRAINT reviews_rating_payment_check,
DROP CONSTRAINT reviews_rating_respect_check;

ALTER TABLE reviews
ADD CONSTRAINT reviews_rating_overall_check CHECK (rating_overall BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_quality_check CHECK (rating_quality BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_punctuality_check CHECK (rating_punctuality BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_communication_check CHECK (rating_communication BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_price_fairness_check CHECK (rating_price_fairness BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_payment_check CHECK (rating_payment BETWEEN 0 AND 5),
ADD CONSTRAINT reviews_rating_respect_check CHECK (rating_respect BETWEEN 0 AND 5);