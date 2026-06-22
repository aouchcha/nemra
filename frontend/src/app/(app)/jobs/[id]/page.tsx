"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { EmptyState, ReviewCard, SectionHeading } from "@/components/site";
import { Badge, Button, Card, Input, Select, Textarea } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CreateReviewDTO, JobDTO, ProviderDTO, ProviderReviewDTO } from "@/lib/types";

export default function JobDetailPage() {
  const params = useParams<{ id: string }>();
  const { user, request } = useAuth();
  const [job, setJob] = useState<JobDTO | null>(null);
  const [provider, setProvider] = useState<ProviderDTO | null>(null);
  const [reviews, setReviews] = useState<ProviderReviewDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [feedback, setFeedback] = useState("");
  const [form, setForm] = useState<CreateReviewDTO>({
    jobId: params.id,
    reviewedId: "",
    reviewerType: user?.role === "PROVIDER" ? "PROVIDER" : "CLIENT",
    comment: "",
    ratingQuality: 5,
    ratingPunctuality: 5,
    ratingCommunication: 5,
    ratingPriceFairness: 5,
    ratingPayment: 5,
    ratingRespect: 5,
  });

  useEffect(() => {
    async function load() {
      try {
        const jobData = await request<JobDTO>(`/api/jobs/${params.id}`);
        setJob(jobData);
        if ("providerId" in jobData) {
          const providerData = await request<ProviderDTO>(`/api/providers/${jobData.providerId}`);
          setProvider(providerData);
          const reviewData = await request<ProviderReviewDTO[]>(`/api/reviews/provider/${jobData.providerId}`);
          setReviews(reviewData);
          if (user?.role === "CLIENT") {
            setForm((current) => ({ ...current, reviewedId: providerData.user_id, reviewerType: "CLIENT", jobId: params.id }));
          }
        }
        if (user?.role === "PROVIDER") {
          setForm((current) => ({ ...current, reviewerType: "PROVIDER", jobId: params.id }));
        }
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load job");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [params.id, request, user?.role]);

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Job details" title={job?.description ?? "Open job"} description="Use accept, complete, cancel, or review actions according to the role returned by the backend." />
      {loading ? <p className="muted">Loading...</p> : null}
      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}

      {job ? (
        <div className="grid-2">
          <Card className="stack-card">
            <div className="card-row">
              <div>
                <p className="card-title">{job.description}</p>
                <p className="muted">Client: {job.clientName}</p>
                {"providerName" in job ? <p className="muted">Provider: {job.providerName}</p> : null}
              </div>
              <Badge tone={job.status === "COMPLETED" ? "success" : job.status === "CANCELLED" ? "danger" : "accent"}>{job.status}</Badge>
            </div>
            {"completedAt" in job ? <p className="muted">Completed: {new Date(job.completedAt).toLocaleString()}</p> : null}
            <div className="button-row">
              {"providerId" in job && job.status === "PENDING" && user?.role === "PROVIDER" ? (
                <Button
                  onClick={async () => {
                    await request(`/api/jobs/${job.id}/accept`, { method: "PATCH" });
                    window.location.reload();
                  }}
                >
                  Accept
                </Button>
              ) : null}
              {"providerId" in job && job.status !== "COMPLETED" && user?.role === "PROVIDER" ? (
                <Button
                  variant="secondary"
                  onClick={async () => {
                    await request(`/api/jobs/${job.id}/complete`, { method: "PATCH" });
                    window.location.reload();
                  }}
                >
                  Complete
                </Button>
              ) : null}
              {job.status !== "COMPLETED" ? (
                <Button
                  variant="ghost"
                  onClick={async () => {
                    await request(`/api/jobs/${job.id}/cancel`, { method: "PATCH" });
                    window.location.reload();
                  }}
                >
                  Cancel
                </Button>
              ) : null}
            </div>
          </Card>

        </div>
      ) : null}

      {provider ? (
        <Card className="stack-card">
          <SectionHeading eyebrow="Linked provider" title={provider.businessName} />
          <p className="muted">{provider.fullName}</p>
          <p className="muted">Provider profile: {provider.providerId}</p>
        </Card>
      ) : null}

      <Card className="stack-card">
        <SectionHeading eyebrow="Reviews" title="Provider reviews for this job context" />
        {reviews.length ? reviews.map((review) => <ReviewCard key={review.id} review={review} />) : <EmptyState title="No reviews returned" />}
      </Card>

      <Card className="stack-card">
        <SectionHeading eyebrow="Submit review" title="Review the finished job" description={job?.status === "COMPLETED" ? "The form is enabled because the backend only accepts reviews for completed jobs." : "Complete the job first before submitting a review."} />
        {job?.status === "COMPLETED" ? (
          <div className="form-grid">
            <div className="field-stack">
              <label className="field-label" htmlFor="reviewedId">Reviewed user id</label>
              <Input id="reviewedId" value={form.reviewedId} onChange={(e) => setForm((current) => ({ ...current, reviewedId: e.target.value }))} />
            </div>
            <div className="field-stack">
              <label className="field-label" htmlFor="reviewerType">Reviewer type</label>
              <Select id="reviewerType" value={form.reviewerType} onChange={(e) => setForm((current) => ({ ...current, reviewerType: e.target.value as "CLIENT" | "PROVIDER" }))}>
                <option value="CLIENT">CLIENT</option>
                <option value="PROVIDER">PROVIDER</option>
              </Select>
            </div>
            <div className="field-stack">
              <label className="field-label" htmlFor="comment">Comment</label>
              <Textarea id="comment" value={form.comment} onChange={(e) => setForm((current) => ({ ...current, comment: e.target.value }))} />
            </div>
            <div className="form-2">
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingQuality">Quality</label>
                <Input id="ratingQuality" type="number" min="0" max="5" value={form.ratingQuality ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingQuality: Number(e.target.value) }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingPunctuality">Punctuality</label>
                <Input id="ratingPunctuality" type="number" min="0" max="5" value={form.ratingPunctuality ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingPunctuality: Number(e.target.value) }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingCommunication">Communication</label>
                <Input id="ratingCommunication" type="number" min="0" max="5" value={form.ratingCommunication ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingCommunication: Number(e.target.value) }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingPriceFairness">Price fairness</label>
                <Input id="ratingPriceFairness" type="number" min="0" max="5" value={form.ratingPriceFairness ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingPriceFairness: Number(e.target.value) }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingPayment">Payment</label>
                <Input id="ratingPayment" type="number" min="0" max="5" value={form.ratingPayment ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingPayment: Number(e.target.value) }))} />
              </div>
              <div className="field-stack">
                <label className="field-label" htmlFor="ratingRespect">Respect</label>
                <Input id="ratingRespect" type="number" min="0" max="5" value={form.ratingRespect ?? 5} onChange={(e) => setForm((current) => ({ ...current, ratingRespect: Number(e.target.value) }))} />
              </div>
            </div>
            <Button
              onClick={async () => {
                setFeedback("");
                try {
                  await request("/api/reviews", { method: "POST", body: form });
                  setFeedback("Review created.");
                } catch (cause) {
                  setFeedback(cause instanceof Error ? cause.message : "Failed to create review");
                }
              }}
            >
              Send review
            </Button>
            {feedback ? <p className="hint">{feedback}</p> : null}
          </div>
        ) : (
          <EmptyState title="Waiting on completion" description="The backend will reject reviews until the job reaches COMPLETED." />
        )}
      </Card>
    </div>
  );
}
