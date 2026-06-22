"use client";

import { MarketingShell } from "@/components/site";
import type { ReactNode } from "react";

export default function MarketingLayout({ children }: Readonly<{ children: ReactNode }>) {
  return <MarketingShell>{children}</MarketingShell>;
}
