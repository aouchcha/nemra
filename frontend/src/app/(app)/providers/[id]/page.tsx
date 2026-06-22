"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { CategoryCard, EmptyState, ReviewCard, SectionHeading } from "@/components/site";
import { Badge, Button, Card, Input, Textarea } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CreateReviewDTO, ProviderDTO, ProviderReviewDTO } from "@/lib/types";

export default function ProviderDetailPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const { request, user } = useAuth();
  const [provider, setProvider] = useState<ProviderDTO | null>(null);
  const [reviews, setReviews] = useState<ProviderReviewDTO[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [reviewForm, setReviewForm] = useState<CreateReviewDTO>({
    jobId: "",
    reviewedId: "",
    reviewerType: "CLIENT",
    comment: "",
    ratingQuality: 5,
    ratingPunctuality: 5,
    ratingCommunication: 5,
    ratingPriceFairness: 5,
  });
  const [reviewFeedback, setReviewFeedback] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const providerData = await request<ProviderDTO>(`/api/providers/${params.id}`);
        const reviewData = await request<ProviderReviewDTO[]>(`/api/reviews/provider/${params.id}`);
        setProvider(providerData);
        setReviews(reviewData);
        if (user?.role === "CLIENT") {
          setReviewForm((current) => ({
            ...current,
            reviewedId: providerData.user_id,
            reviewerType: "CLIENT",
          }));
        }
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load provider");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [params.id, request, user?.role]);

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Provider" title={provider?.businessName ?? "Provider details"} description={provider?.bio ?? "Profile loaded from GET /api/providers/{id} and review history from the reviews endpoint."} />

      {loading ? <p className="muted">Loading...</p> : null}
      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}

      {provider ? (
        <div className="grid-2">
          <Card className="stack-card">
            <div className="card-row">
              <div>
                <p className="card-title">{provider.fullName}</p>
                <p className="muted">{provider.city}</p>
              </div>
              <Badge tone={provider.isVerified ? "success" : "neutral"}>{provider.isVerified ? "verified" : "not verified"}</Badge>
            </div>
            {provider.category ? <CategoryCard category={provider.category} /> : <EmptyState title="No category attached" />}
            <p className="muted">Average rating: {provider.averageRating.toFixed(1)}</p>
            <p className="muted">Reviews: {provider.totalReviews}</p>
            <p className="muted">Provider ID: {provider.providerId}</p>
            <Button variant="secondary" onClick={() => router.push("/calls")}>Open call tool</Button>
          </Card>

          <Card className="stack-card">
            <SectionHeading
              eyebrow="Review"
              title="Submit a client review"
              description={
                user?.role === "CLIENT"
                  ? "The provider user id is prefilled from the provider profile endpoint."
                  : "Provider reviews need the job context, so use the job detail page."
              }
            />
            {user?.role === "CLIENT" ? (
              <div className="form-grid">
                <div className="field-stack">
                  <label className="field-label" htmlFor="jobId">Job id</label>
                  <Input id="jobId" value={reviewForm.jobId} onChange={(e) => setReviewForm((current) => ({ ...current, jobId: e.target.value }))} placeholder="Paste a completed job id" />
                </div>
                <div className="field-stack">
                  <label className="field-label" htmlFor="reviewedId">Reviewed user id</label>
                  <Input id="reviewedId" value={reviewForm.reviewedId} onChange={(e) => setReviewForm((current) => ({ ...current, reviewedId: e.target.value }))} />
                </div>
                <div className="field-stack">
                  <label className="field-label" htmlFor="comment">Comment</label>
                  <Textarea id="comment" value={reviewForm.comment} onChange={(e) => setReviewForm((current) => ({ ...current, comment: e.target.value }))} />
                </div>
                <div className="form-2">
                  <div className="field-stack">
                    <label className="field-label" htmlFor="ratingQuality">Quality</label>
                    <Input id="ratingQuality" type="number" min="0" max="5" value={reviewForm.ratingQuality ?? 5} onChange={(e) => setReviewForm((current) => ({ ...current, ratingQuality: Number(e.target.value) }))} />
                  </div>
                  <div className="field-stack">
                    <label className="field-label" htmlFor="ratingPunctuality">Punctuality</label>
                    <Input id="ratingPunctuality" type="number" min="0" max="5" value={reviewForm.ratingPunctuality ?? 5} onChange={(e) => setReviewForm((current) => ({ ...current, ratingPunctuality: Number(e.target.value) }))} />
                  </div>
                  <div className="field-stack">
                    <label className="field-label" htmlFor="ratingCommunication">Communication</label>
                    <Input id="ratingCommunication" type="number" min="0" max="5" value={reviewForm.ratingCommunication ?? 5} onChange={(e) => setReviewForm((current) => ({ ...current, ratingCommunication: Number(e.target.value) }))} />
                  </div>
                  <div className="field-stack">
                    <label className="field-label" htmlFor="ratingPriceFairness">Price fairness</label>
                    <Input id="ratingPriceFairness" type="number" min="0" max="5" value={reviewForm.ratingPriceFairness ?? 5} onChange={(e) => setReviewForm((current) => ({ ...current, ratingPriceFairness: Number(e.target.value) }))} />
                  </div>
                </div>
                <Button
                  onClick={async () => {
                    setReviewFeedback("");
                    try {
                      await request("/api/reviews", {
                        method: "POST",
                        body: {
                          ...reviewForm,
                          reviewerType: "CLIENT",
                        },
                      });
                      setReviewFeedback("Review created.");
                    } catch (cause) {
                      setReviewFeedback(cause instanceof Error ? cause.message : "Failed to create review");
                    }
                  }}
                >
                  Send review
                </Button>
                {reviewFeedback ? <p className="hint">{reviewFeedback}</p> : null}
              </div>
            ) : (
              <p className="muted">This screen is optimized for client reviews. Use the job detail page to submit provider-side reviews.</p>
            )}
          </Card>
        </div>
      ) : null}

      <Card className="stack-card">
        <SectionHeading eyebrow="Reviews" title="Provider review history" />
        {reviews.length ? reviews.map((review) => <ReviewCard key={review.id} review={review} />) : <EmptyState title="No reviews returned" />}
      </Card>
    </div>
  );
}
