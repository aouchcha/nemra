"use client";

"use client";

import Link from "next/link";
import { SectionHeading } from "@/components/site";
import { Badge, Card, buttonClass } from "@/components/ui";

const cards = [
  {
    href: "/register/client",
    title: "Client account",
    text: "Create jobs, follow provider progress, and leave reviews after completion.",
  },
  {
    href: "/register/provider",
    title: "Provider account",
    text: "Build a public profile, join categories, and receive job requests.",
  },
];

export default function RegisterChooserPage() {
  return (
    <main className="hero" style={{ paddingTop: "1.25rem" }}>
      <div className="hero-copy" style={{ maxWidth: "1180px", margin: "0 auto" }}>
        <Badge tone="accent">Registration</Badge>
        <SectionHeading
          title="Choose the account type you want to create"
          description="The backend exposes separate flows for clients and providers, so the UI keeps those paths explicit."
        />
        <div className="grid-2" style={{ marginTop: "1rem" }}>
          {cards.map((card) => (
            <Card key={card.href} className="stack-card">
              <p className="card-title">{card.title}</p>
              <p className="muted">{card.text}</p>
              <Link href={card.href} className={buttonClass("secondary")}>
                Continue
              </Link>
            </Card>
          ))}
        </div>
      </div>
    </main>
  );
}
