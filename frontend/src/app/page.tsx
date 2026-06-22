"use client";

import Link from "next/link";
import { MarketingShell } from "@/components/site";
import { Badge, Card, buttonClass } from "@/components/ui";

const pillars = [
  {
    title: "Fast provider discovery",
    text: "Browse verified providers with ratings, categories, and location-aware profiles.",
  },
  {
    title: "Job lifecycle",
    text: "Create jobs, accept work, complete tasks, and keep status changes visible.",
  },
  {
    title: "Calls and reviews",
    text: "Generate LiveKit call tokens and submit structured reviews after completion.",
  },
];

export default function HomePage() {
  return (
    <MarketingShell>
      <main className="hero">
        <div className="hero-grid">
          {/*<section className="hero-copy">*/}
            {/*<Badge tone="accent">Next.js frontend</Badge>*/}
            {/*<h1>Market services without adding UI friction.</h1>*/}
            {/*<p>*/}
            {/*  Nemra is a black, white, and orange service marketplace UI connected to the Spring Boot backend.*/}
            {/*  It covers login, provider discovery, jobs, reviews, calls, profile management, and admin category tools.*/}
            {/*</p>*/}
            <div className="hero-actions">
              <Link href="/register" className={buttonClass("primary")}>
                Create account
              </Link>
              <Link href="/login" className={buttonClass("secondary")}>
                Sign in
              </Link>
              {/*<Link href="/providers" className={buttonClass("ghost")}>*/}
              {/*  View providers*/}
              {/*</Link>*/}
            </div>
            {/*<div className="hero-metrics" style={{ marginTop: "1.5rem" }}>*/}
            {/*  <Card className="mini-panel">*/}
            {/*    <p className="eyebrow">Auth</p>*/}
            {/*    <p className="card-title">JWT sessions</p>*/}
            {/*    <p className="muted">Access and refresh token handling built in.</p>*/}
            {/*  </Card>*/}
            {/*  <Card className="mini-panel">*/}
            {/*    <p className="eyebrow">LiveKit</p>*/}
            {/*    <p className="card-title">Call token flow</p>*/}
            {/*    <p className="muted">Generate room tokens from the UI.</p>*/}
            {/*  </Card>*/}
            {/*  <Card className="mini-panel">*/}
            {/*    <p className="eyebrow">Roles</p>*/}
            {/*    <p className="card-title">Client, provider, admin</p>*/}
            {/*    <p className="muted">Role-aware navigation and actions.</p>*/}
            {/*  </Card>*/}
            {/*</div>*/}
          {/*</section>*/}

          {/*<aside className="hero-aside">*/}
          {/*  {pillars.map((item) => (*/}
          {/*    <Card key={item.title} className="mini-panel">*/}
          {/*      <p className="card-title">{item.title}</p>*/}
          {/*      <p className="muted">{item.text}</p>*/}
          {/*    </Card>*/}
          {/*  ))}*/}
          {/*  <div className="mini-panel">*/}
          {/*    <p className="eyebrow">Backend endpoint</p>*/}
          {/*    <p className="card-title">http://localhost:8080</p>*/}
          {/*    <p className="muted">Override via `NEXT_PUBLIC_API_BASE_URL`.</p>*/}
          {/*  </div>*/}
          {/*</aside>*/}
        </div>
      </main>
    </MarketingShell>
  );
}
