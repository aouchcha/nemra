"use client";

import { useEffect, useState } from "react";
import { EmptyState, JobCard, SectionHeading } from "@/components/site";
import { Button, Card, Input, Textarea } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CreateJobRequest, JobDTO } from "@/lib/types";

export default function JobsPage() {
  const { user, request } = useAuth();
  const [jobs, setJobs] = useState<JobDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [description, setDescription] = useState("");
  const [feedback, setFeedback] = useState("");

  useEffect(() => {
    async function load() {
      try {
        const data = await request<JobDTO[]>("/api/jobs/my");
        setJobs(data);
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load jobs");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [request]);

  return (
    <div className="page-grid">
      <SectionHeading eyebrow="Jobs" title="Your job queue" description="This page talks to /api/jobs/my and lets CLIENT accounts create jobs." />

      {user?.role === "CLIENT" ? (
        <Card className="input-shell">
          <p className="card-title">Create a job</p>
          <Textarea value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Describe the service you need" />
          <Button
            onClick={async () => {
              setFeedback("");
              try {
                const payload: CreateJobRequest = { description };
                await request("/api/jobs", { method: "POST", body: payload });
                setFeedback("Job created.");
                setDescription("");
                const refreshed = await request<JobDTO[]>("/api/jobs/my");
                setJobs(refreshed);
              } catch (cause) {
                setFeedback(cause instanceof Error ? cause.message : "Failed to create job");
              }
            }}
          >
            Submit job
          </Button>
          {feedback ? <p className="hint">{feedback}</p> : null}
        </Card>
      ) : null}

      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}
      {loading ? <p className="muted">Loading...</p> : jobs.length ? <div className="grid-2">{jobs.map((job) => <JobCard key={job.id} job={job} />)}</div> : <EmptyState title="No jobs returned" />}
    </div>
  );
}
