"use client";

import { useEffect, useState } from "react";
import { CategoryCard, EmptyState, JobCard, ProviderCard, SectionHeading, StatCard } from "@/components/site";
import { Button, Card } from "@/components/ui";
import { useAuth } from "@/contexts/auth-context";
import type { CategoryDTO, JobDTO, ProviderSummaryDTO } from "@/lib/types";

export default function DashboardPage() {
  const { user, request } = useAuth();
  const [providers, setProviders] = useState<ProviderSummaryDTO[]>([]);
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [jobs, setJobs] = useState<JobDTO[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const [providerData, categoryData, jobData] = await Promise.all([
          request<ProviderSummaryDTO[]>("/api/providers"),
          request<CategoryDTO[]>("/api/categories"),
          request<JobDTO[]>("/api/jobs/my"),
        ]);
        setProviders(providerData);
        setCategories(categoryData);
        setJobs(jobData);
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : "Failed to load dashboard");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [request]);

  return (
    <div className="page-grid">
      <SectionHeading
        eyebrow="Dashboard"
        title={`Welcome back, ${user?.fullName ?? "user"}`}
        description="The page aggregates the authenticated endpoints already present in the backend."
      />

      <div className="stats-grid">
        <StatCard label="Providers" value={providers.length} hint="From GET /api/providers" />
        <StatCard label="Categories" value={categories.length} hint="From GET /api/categories" />
        <StatCard label="Jobs" value={jobs.length} hint="From GET /api/jobs/my" />
      </div>

      <div className="grid-2">
        <Card className="stack-card">
          <SectionHeading eyebrow="Quick actions" title="Move fast" />
          <div className="button-row">
            <Button variant="secondary" onClick={() => window.location.assign("/providers")}>Browse providers</Button>
            <Button variant="secondary" onClick={() => window.location.assign("/jobs")}>Open jobs</Button>
            <Button variant="secondary" onClick={() => window.location.assign("/profile")}>Edit profile</Button>
          </div>
        </Card>
        <Card className="stack-card">
          <SectionHeading eyebrow="Session" title="Current user" />
          <p className="card-title">{user?.fullName}</p>
          <p className="muted">{user?.phone}</p>
          <p className="muted">{user?.city}</p>
        </Card>
      </div>

      {error ? <Card className="stack-card"><p className="card-title">Failed to load</p><p className="muted">{error}</p></Card> : null}

      <div className="grid-2">
        <Card className="stack-card">
          <SectionHeading eyebrow="Categories" title="Current categories" />
          {loading ? <p className="muted">Loading...</p> : categories.length ? categories.slice(0, 4).map((category) => <CategoryCard key={category.id} category={category} />) : <EmptyState title="No categories returned" />}
        </Card>
        <Card className="stack-card">
          <SectionHeading eyebrow="Providers" title="Featured providers" />
          {loading ? <p className="muted">Loading...</p> : providers.length ? providers.slice(0, 3).map((provider) => <ProviderCard key={provider.id} provider={provider} />) : <EmptyState title="No providers returned" />}
        </Card>
      </div>

      <Card className="stack-card">
        <SectionHeading eyebrow="Jobs" title="Recent jobs" />
        {loading ? <p className="muted">Loading...</p> : jobs.length ? jobs.slice(0, 3).map((job) => <JobCard key={job.id} job={job} />) : <EmptyState title="No jobs yet" description="Create one from the jobs page when the session belongs to a client." />}
      </Card>
    </div>
  );
}

