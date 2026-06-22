"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useAuth } from "@/contexts/auth-context";
import type { ReactNode } from "react";
import type {
  CategoryDTO,
  JobDTO,
  ProviderSummaryDTO,
  ReviewResponseDTO,
  UserDTO,
} from "@/lib/types";
import { Badge, Button, Card, buttonClass } from "./ui";

export function MarketingShell({ children }: { children: ReactNode }) {
  return (
    <div className="site-shell">
      <header className="marketing-topbar">
        <Link href="/" className="brand-mark">
          <span className="brand-dot" />
          <span>Nemra</span>
        </Link>
        {/*<nav className="marketing-actions">*/}
        {/*  <Link href="/providers" className={buttonClass("ghost")}>*/}
        {/*    Explore providers*/}
        {/*  </Link>*/}
        {/*  <Link href="/login" className={buttonClass("secondary")}>*/}
        {/*    Sign in*/}
        {/*  </Link>*/}
        {/*</nav>*/}
      </header>
      {children}
      {/*<footer className="marketing-footer">*/}
      {/*  <div>*/}
      {/*    <p className="eyebrow">Built for service marketplaces</p>*/}
      {/*    <p className="muted">Black, white, and orange system for web today and mobile next.</p>*/}
      {/*  </div>*/}
      {/*  <p className="muted">Nemra backend compatible.</p>*/}
      {/*</footer>*/}
    </div>
  );
}

export function AppShell({ children }: { children: ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const { user, logout } = useAuth();

  const links = [
    { href: "/dashboard", label: "Dashboard" },
    { href: "/providers", label: "Providers" },
    { href: "/categories", label: "Categories" },
    { href: "/jobs", label: "Jobs" },
    { href: "/calls", label: "Calls" },
    { href: "/profile", label: "Profile" },
  ];

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link href="/dashboard" className="brand-mark brand-mark--sidebar">
          <span className="brand-dot" />
          <span>Nemra</span>
        </Link>
        <p className="sidebar-copy">Service marketplace dashboard</p>
        <nav className="sidebar-nav">
          {links.map((link) => {
            const active = pathname === link.href || pathname.startsWith(`${link.href}/`);
            return (
              <Link key={link.href} href={link.href} className={`sidebar-link ${active ? "sidebar-link--active" : ""}`}>
                {link.label}
              </Link>
            );
          })}
          {user?.role === "ADMIN" ? (
            <Link href="/admin/categories" className={`sidebar-link ${pathname.startsWith("/admin") ? "sidebar-link--active" : ""}`}>
              Admin categories
            </Link>
          ) : null}
        </nav>
        <div className="sidebar-card">
          <Badge tone="accent">{user?.role ?? "Guest"}</Badge>
          <p>{user?.fullName ?? "Not signed in"}</p>
          <p className="muted">{user?.phone ?? "Connect the backend session"}</p>
        </div>
      </aside>

      <div className="content-shell">
        <header className="content-topbar">
          <div>
            <p className="eyebrow">Workspace</p>
            <h1 className="content-title">Connected to the backend API</h1>
          </div>
          <div className="topbar-actions">
            <Button
              variant="secondary"
              onClick={async () => {
                await logout();
                router.push("/login");
              }}
            >
              Switch account
            </Button>
            <Button
              variant="ghost"
              onClick={async () => {
                await logout();
                router.push("/");
              }}
            >
              Log out
            </Button>
          </div>
        </header>

        <main className="content-main">{children}</main>
      </div>
    </div>
  );
}

export function SectionHeading({
  eyebrow,
  title,
  description,
}: {
  eyebrow?: string;
  title: string;
  description?: string;
}) {
  return (
    <div className="section-heading">
      {eyebrow ? <p className="eyebrow">{eyebrow}</p> : null}
      <h2>{title}</h2>
      {description ? <p className="muted">{description}</p> : null}
    </div>
  );
}

export function StatCard({
  label,
  value,
  hint,
}: {
  label: string;
  value: string | number;
  hint?: string;
}) {
  return (
    <Card className="stat-card">
      <p className="stat-label">{label}</p>
      <p className="stat-value">{value}</p>
      {hint ? <p className="muted">{hint}</p> : null}
    </Card>
  );
}

export function CategoryCard({ category }: { category: CategoryDTO }) {
  return (
    <Card className="stack-card">
      <div className="card-row">
        <div>
          <p className="card-title">{category.nameEn}</p>
          <p className="muted">{category.nameFr} · {category.nameAr}</p>
        </div>
        <Badge tone={category.isActive ? "success" : "danger"}>{category.isActive ? "active" : "inactive"}</Badge>
      </div>
      <p className="muted">Created {new Date(category.createdAt).toLocaleDateString()}</p>
    </Card>
  );
}

export function ProviderCard({ provider }: { provider: ProviderSummaryDTO }) {
  return (
    <Card className="stack-card">
      <div className="card-row">
        <div className="avatar-circle">{provider.fullName.slice(0, 1).toUpperCase()}</div>
        <div className="grow">
          <p className="card-title">{provider.businessName}</p>
          <p className="muted">
            {provider.fullName} · {provider.category?.nameEn ?? "Uncategorized"}
          </p>
        </div>
        <Badge tone={provider.isVerified ? "success" : "neutral"}>{provider.isVerified ? "verified" : "standard"}</Badge>
      </div>
      <div className="card-meta">
        <span>★ {provider.averageRating.toFixed(1)}</span>
        <span>{provider.category?.nameAr ?? "No Arabic name"}</span>
      </div>
      <Link href={`/providers/${provider.id}`} className={buttonClass("secondary")}>
        View provider
      </Link>
    </Card>
  );
}

export function JobCard({ job }: { job: JobDTO }) {
  const providerName = "providerName" in job ? job.providerName : null;
  const completedAt = "completedAt" in job ? job.completedAt : null;

  return (
    <Card className="stack-card">
      <div className="card-row">
        <div>
          <p className="card-title">{job.description || "Untitled job"}</p>
          <p className="muted">
            {job.clientName}
            {providerName ? ` · ${providerName}` : ""}
          </p>
        </div>
        <Badge tone={job.status === "COMPLETED" ? "success" : job.status === "CANCELLED" ? "danger" : "accent"}>
          {job.status}
        </Badge>
      </div>
      <div className="card-meta">
        <span>Job #{job.id.slice(0, 8)}</span>
        <span>{new Date(job.createdAt).toLocaleDateString()}</span>
        {completedAt ? <span>Completed {new Date(completedAt).toLocaleDateString()}</span> : null}
      </div>
      <Link href={`/jobs/${job.id}`} className={buttonClass("secondary")}>
        Open job
      </Link>
    </Card>
  );
}

export function ReviewCard({ review }: { review: ReviewResponseDTO }) {
  return (
    <Card className="stack-card">
      <div className="card-row">
        <div>
          <p className="card-title">
            {review.reviewerName} <span className="muted">to {review.reviewedName}</span>
          </p>
          <p className="muted">{new Date(review.createdAt).toLocaleString()}</p>
        </div>
        <Badge tone="accent">★ {review.ratingOverall}</Badge>
      </div>
      <p className="review-copy">{review.comment}</p>
    </Card>
  );
}

export function EmptyState({
  title,
  description,
  action,
}: {
  title: string;
  description?: string;
  action?: ReactNode;
}) {
  return (
    <Card className="empty-state">
      <p className="card-title">{title}</p>
      {description ? <p className="muted">{description}</p> : null}
      {action ? <div>{action}</div> : null}
    </Card>
  );
}
