# AWS Cloud Practitioner (CLF-C02) — Study Notes
*Condensed from "Pass AWS Cloud Practitioner in 100 Minutes"*

---

## 1. Cloud Concepts (~26% of exam)

**The Cloud = a utility.** Old way (on-premises) = buying your own generator: huge upfront cost, you maintain it. That's **CapEx** (capital expenditure). Cloud way = plugging into the power grid: pay only for what you use. That's **OpEx** (operational expenditure).

> 🎯 **Golden rule:** the shift from CapEx → OpEx is one of the most tested ideas on the exam.

### The Six Cloud "Superpowers"
| Benefit | What it means |
|---|---|
| **Agility** | Launch/kill resources in minutes, not months — fast, low-risk experimentation |
| **Elasticity** | Automatically scale **up AND down** to match real-time demand |
| **High Availability** | Redundant systems so failures don't cause downtime |
| **Global Reach** | Deploy to AWS Regions worldwide in a few clicks — low latency for global users |
| **Economies of Scale** | AWS buys hardware at massive scale → passes discounts to you |
| **Pay As You Go** | No contracts, no idle capacity — pay only for what you consume |

### ⚠️ Exam Trap: Elasticity vs. Scalability
- **Elasticity** = grows **and shrinks** automatically (rubber band). The "shrinks back down to save money" part is what makes it elastic.
- **Scalability** = just handling more load (adding lanes to a highway) — doesn't necessarily shrink back.
- On the exam: "autoscaling" → think **elasticity**.

---

## 2. Cloud Deployment Models

| Model | Analogy | Keywords |
|---|---|---|
| **Public Cloud** | Renting an apartment (share infrastructure, provider fixes issues) | High scalability/elasticity, OpEx, no upfront cost, global reach |
| **Private Cloud** | Owning a house (full control, full responsibility) | Maximum control & security, CapEx, big upfront investment, on-premises |
| **Hybrid Cloud** | House (private, sensitive data) + rented storage unit (public, overflow) | Best of both worlds |

**Connecting hybrid cloud pieces:**
- **AWS VPN** — secure tunnel over the public internet
- **AWS Direct Connect** — dedicated private physical connection (faster, more secure)

> ⚠️ **Exam Trap:** Hybrid cloud ≠ Multicloud.
> - Hybrid = private (on-prem) **+** public cloud
> - Multicloud = using **multiple public clouds** (e.g., AWS + Google Cloud) — no private data center involved

---

## 3. Cloud Service Models (IaaS / PaaS / SaaS)

The only real difference between these models = **where the line is drawn between what you manage vs. what AWS manages.**

**Pizza-as-a-Service analogy:**

| Model | Pizza analogy | You manage | AWS manages | Example |
|---|---|---|---|---|
| **On-prem** | Make pizza from scratch | Everything | Nothing | Traditional data center |
| **IaaS** | Take-and-bake pizza | OS, patches, apps | Servers, storage, networking | **Amazon EC2** |
| **PaaS** | Pizza delivery | Your code & data | Hardware + OS | **Amazon RDS** |
| **SaaS** | Dining out | Nothing | Everything | **AWS WorkSpaces** |

---

## 4. AWS Global Infrastructure

- **Availability Zone (AZ):** one or more physically separate, independent data centers (own power, cooling, network).
- **Region:** a physical area (e.g., us-east-1 = N. Virginia) made up of multiple AZs. **A region always has ≥ 2 AZs.**

**Analogy:** Region = a city; each AZ = a separate pizza kitchen in that city. One kitchen's oven breaks → the others keep delivering.

| Problem scope | Solution |
|---|---|
| Single data center failure | Multiple **AZs** |
| Whole region/city disaster, data sovereignty, latency to users | Multiple **Regions** |

> 🎯 **The magic number = 2.** High availability requires **at least 2 AZs** — never just one.

> ⚠️ **Exam Trap:** "10 servers in a single AZ" is **NOT** highly available — an entire AZ can fail (fire, flood, power outage). Redundancy must span AZs, not just servers.

---

## 5. High Availability (deep dive)

**Definition:** system built to handle problems and keep running without interruption ("always on").

**Coffee shop analogy:** one barista (single point of failure) → hire a second barista (redundancy) → shop stays open even if one calls in sick.

Mapping: barista = EC2 instance; coffee shop building = one Availability Zone.

> 🎯 High availability = **spread your app across ≥ 2 AZs.** One AZ is never enough.

---

## 6. Identity & Access Management (IAM) — part of Security domain (~30%)

IAM = the security guard for your AWS account: decides who gets in and what they can touch.

| Concept | Analogy | Key trait |
|---|---|---|
| **User** | Permanent employee badge | **Long-term** credentials (password/access keys) |
| **Group** | "Marketing access level" | Collection of users — apply permissions once, everyone gets them |
| **Role** | Temporary visitor pass | **Temporary** credentials, assumed by a user/service, auto-expire — most secure |

### Key IAM Principles (exam answers)
1. **Least privilege** — minimum permissions needed, nothing more
2. **Protect the root user** — never use for daily tasks
3. **Use MFA** — especially on root
4. **Use groups** to manage permissions at scale
5. **Use roles** for secure, temporary access (especially for applications)

> ⚠️ **Exam Trap:** EC2 app needs S3 access — correct answer is always **attach an IAM Role** to the EC2 instance (temporary, rotating credentials). Hard-coding an IAM user's permanent access keys into app code = classic wrong answer / security risk.

> **Cheat sheet:** User = permanent. Role = temporary.

---

## 7. Security Services — Perimeter vs. Internal

**Mansion party analogy:**

| Service | Role | Keyword trigger |
|---|---|---|
| **AWS Shield** | Barricades at the edge of the property — stops a mob (volume) | **DDoS** attacks (Shield Standard = free, automatic) |
| **AWS WAF** | Bouncer at the front door — checks IDs/bags (content) | **Web Application Firewall**, Layer 7, **SQL injection**, **cross-site scripting (XSS)** |
| **Amazon GuardDuty** | Undercover guard mingling with guests — watches for suspicious behavior | Intelligent threat detection, continuous monitoring, ML |
| **Amazon Inspector** | Pre-party safety inspector — checks for weaknesses beforehand | **Vulnerability assessment**, scanning EC2 for known vulnerabilities |

> ⚠️ **Exam Trap:** Shield = **quantity** of traffic (DDoS). WAF = **quality**/content of traffic (malicious code, XSS, SQLi). If the question mentions XSS/SQLi → **WAF**, not Shield.

---

## 8. Encryption: At Rest vs. In Transit

| State | Analogy | AWS Service |
|---|---|---|
| **At rest** (stored, e.g., in S3) | Data in a safe | **AWS KMS** (Key Management Service) |
| **In transit** (moving, e.g., browser → server) | Data in an armored truck | **SSL/TLS** via **AWS Certificate Manager (ACM)** |

> ⚠️ **Exam Trap:** "How do you protect data while it is being **uploaded** to S3?" — "uploaded" = **in transit**, not at rest (it only becomes "at rest" once saved in the bucket). Always check: is the question about the *journey* or the *destination*?

---

## 9. Governance & Compliance (~30% domain)

**Framework:** Governance = the rules. Compliance = the proof you followed them. (Fire code = governance; fire marshal's inspection certificate = compliance.)

| Service | Answers | Think of it as |
|---|---|---|
| **AWS Config** | *What* is the current configuration/state of my resources? | Configuration snapshot checker |
| **AWS CloudTrail** | *Who* did what, when, from where? | Action/user activity log (detective) |
| **AWS Artifact** | Where do I get official reports (SOC reports, agreements)? | On-demand compliance report library |
| **AWS Security Hub** | Where's my centralized view of security alerts? | Security dashboard |
| **AWS Audit Manager** | How do I gather evidence for an audit/assessment? | Evidence collector |

> ⚠️ **Exam Trap:** "Who deleted our S3 bucket last Tuesday?" → this is about a **person's action** → **CloudTrail**, not Config.
> **Mnemonic:** CloudTrail = trail of **users**. Config = configuration of **resources**.

---

## 10. EC2, Pricing Options & Autoscaling

**EC2** = Elastic Compute Cloud = a virtual server ("renting a car" — pick the size you need, give it back when done).

### Three EC2 Pricing Models
| Model | Analogy | Best for | Trade-off |
|---|---|---|---|
| **On-Demand** | Pay-as-you-go phone plan | Unpredictable/spiky traffic, just starting out | Flexible but priciest |
| **Reserved** | 1–3 yr car lease | Steady, predictable, 24/7 critical workloads (e.g., a critical database) | Up to ~72% discount, but locked into a commitment |
| **Spot** | Bidding on spare capacity | Interruptible batch jobs (e.g., overnight data processing) | Cheapest (up to ~90% off), but AWS can reclaim it with ~2 min notice |

> ⚠️ **Exam Trap:** "Critical database, predictable traffic, running 24/7 for 3 years, maximum savings" → don't jump to Spot just because it sounds cheapest! **Reserved** is correct — a mission-critical DB can never be interrupted like a Spot instance can.

### Autoscaling
- Solves: paying for idle servers at 3am / not enough capacity when traffic spikes.
- Built around **elasticity** (adds/removes servers automatically to match demand).
- You configure: **minimum** servers, **maximum** servers, and a **trigger** (e.g., "if avg CPU > 70%, add an instance").

---

## 11. Amazon S3 & Storage Classes

**S3 = object storage** — a "digital warehouse" for whole files (photos, videos, backups). Not a hard drive — **you cannot install an OS on S3.**

| Storage Class | Analogy | Use case | Access speed / cost |
|---|---|---|---|
| **S3 Standard** | Hot storage | Frequently accessed data (website images) | Instant, most expensive |
| **S3 Infrequent Access (IA)** | Occasional-use storage | Backups, rarely touched data | Instant access, lower storage cost |
| **S3 Glacier** | Deep freezer | Long-term archiving, legal/compliance retention | Minutes–hours to retrieve, cheapest |

> 🎯 **Rule of thumb:** the faster you need it, the more it costs.

> ⚠️ **Exam Trap:** "Where do you install the OS for a new web server?" → **EBS**, never S3. S3 holds content (images/videos); it is not a bootable drive.

---

## 12. Storage Types: Block (EBS) vs. File (EFS) vs. Object (S3)

| Type | Analogy | AWS Service | Key trait |
|---|---|---|---|
| **Block storage** | Your computer's C: drive | **EBS** (Elastic Block Store) | Attached to **one** EC2 instance only — fast, low-latency |
| **File storage** | Shared office network drive | **EFS** (Elastic File System) | **Shared** access — many servers/computers at once |
| **Object storage** | Google Drive/Dropbox | **S3** | Accessed over the internet from anywhere, holds whole files |

> 🎯 **Classic question:** "High-performance database on a **single EC2 instance**, low-latency disk access" → keyword "single instance" + "fast" = **EBS**.

---

## 13. VPC (Virtual Private Cloud)

**VPC** = a logically isolated section of AWS you carve out and control (IPs, subnets, routes). Analogy: your own private office building inside the giant "city" of AWS's global infrastructure.

### Two Layers of VPC Security
| Layer | Analogy | Scope | Stateful or Stateless? |
|---|---|---|---|
| **Security Group** | Personal bouncer at one office door | Individual **instance** | **Stateful** — remembers connections; only need an inbound rule, return traffic auto-allowed |
| **Network ACL (NACL)** | Guard for the whole floor | Entire **subnet** | **Stateless** — no memory; must define rules for **both** inbound and outbound traffic |

> ⚠️ **Exam Trap:** Security groups vs. NACLs — remember "stateful remembers, stateless forgets (checks both directions)."

---

## 14. Subnets: Public vs. Private

VPC = house; subnets = individual rooms.

| Subnet type | Analogy | Requirement |
|---|---|---|
| **Public subnet** | Living room with a front door to the internet | Has a route to an **Internet Gateway (IGW)** |
| **Private subnet** | Locked vault/safe room | **No** route to the internet |

> ⚠️ **Exam Trap:** Having a "door" (IGW route) ≠ being unsecured. A subnet's route table controls the *path*; a **security group** controls *who's allowed* through that path. You need both.
> **The only thing that makes a subnet public is its route table having a route to an IGW** — not its name, not intent.

---

## 15. Route Tables

- A route table = a list of rules ("routes"), each with a **destination** (where traffic wants to go) and a **target** (where to send it).
- Every subnet must be associated with exactly **one** route table (default = the VPC's main route table if you don't assign a custom one). One route table **can** serve multiple subnets.
- Analogy: route table = GPS for the VPC "city."

> ⚠️ **Exam Trap:** "EC2 instance can't reach the internet, but its security group allows all outbound traffic" → the problem is almost always the **route table** (missing route to IGW), not the security group.

---

## 16. Databases: RDS vs. EC2 (DIY) vs. DynamoDB

### Amazon RDS (Relational Database Service)
- **Managed** relational database service (MySQL, PostgreSQL, SQL Server, etc.) — AWS handles patching, backups, scaling.
- Analogy: EC2-hosted DB = cooking at home (total control, total work). RDS = eating at a restaurant (AWS does the work).
- **Keywords → RDS:** "reduce operational overhead," "managed database solution," relational data.

> ⚠️ **Exam Trap:** If the question mentions needing **"root access"** or **"full control of the OS,"** RDS is wrong — you'd need a database self-hosted on **EC2** instead, since RDS being "managed" means you don't get OS-level access.

### Amazon DynamoDB
- **Fully managed, NoSQL** database.
- Analogy: RDS = a neat, rigid spreadsheet (same columns every row). DynamoDB = a messy filing cabinet (flexible schema, any structure per item).
- **Keywords → DynamoDB:** "fully managed," "NoSQL," flexible/unstructured data, needs to scale to any size (e.g., mobile game profiles, product catalogs with varying attributes).
- Choose **RDS/Aurora** instead when data is highly structured and consistent (e.g., bank ledgers, inventory systems).

---

## 17. AWS Pricing Fundamentals

**Golden rule: you only pay for what you use.**

Three main billed dimensions:
1. **Compute** (e.g., EC2 running time)
2. **Storage** (e.g., S3 space used)
3. **Data transfer out** (see trap below)

### Compute pricing recap
| Model | Best for |
|---|---|
| On-Demand | Unpredictable, spiky workloads |
| Reserved / Savings Plans | Steady 24/7 workloads, 1–3 yr commitment |
| Spot | Interruptible batch jobs, up to ~90% off |

### Cost Management Tools
| Tool | Role |
|---|---|
| **Cost Explorer** | "Accountant" — visualize where money went/will go |
| **AWS Budgets** | "Parent setting an allowance" — alerts when nearing a spending threshold |
| **Trusted Advisor** | "Savvy consultant" — recommends ways to save money / best practices |

> ⚠️ **Exam Trap (most important pricing rule):** **Data transfer IN to AWS is free. Data transfer OUT to the internet costs money.**

---

## 18. AWS Support Plans (~12% within Security & Compliance-adjacent content)

**Cell phone plan analogy** — more money = faster, more personal support:

| Plan | Analogy | Access |
|---|---|---|
| **Basic** | Free Wi-Fi at a coffee shop | Self-service only (forums) |
| **Developer** | Step up a tier | Email access to a real person |
| **Business** | | Phone support, 24/7 |
| **Enterprise On-Ramp** | | Faster response, some TAM-like support |
| **Enterprise** | | Dedicated Technical Account Manager (TAM) |

> ⚠️ **Exam Trap:** Don't confuse **free self-service resources** (whitepapers, blogs, docs, Knowledge Center) with **paid support plans**. If a question asks about getting help from a **real human/AWS expert**, the answer is always one of the **paid plans** — never a blog or whitepaper.

---

## 🔑 Master Cheat-Sheet (Quick Recall Before the Exam)

- **CapEx vs OpEx** → Private/on-prem = CapEx; Public cloud = OpEx
- **Elasticity** = grows *and shrinks*; **Scalability** = just handles growth
- **HA** = minimum **2 Availability Zones**
- **IAM User** = permanent; **IAM Role** = temporary (most secure)
- **Shield** = DDoS/volume; **WAF** = malicious content (SQLi/XSS)
- **KMS** = data at rest; **ACM/SSL-TLS** = data in transit
- **Config** = what (resource state); **CloudTrail** = who (user actions)
- **On-Demand** = flexible; **Reserved** = commitment/steady; **Spot** = cheap but interruptible
- **S3** = object storage (whole files, not bootable); **EBS** = block storage (single instance, bootable); **EFS** = file storage (shared across instances)
- **Security Groups** = stateful, per-instance; **NACLs** = stateless, per-subnet
- **Public subnet** = route to an Internet Gateway; nothing more, nothing less
- **RDS** = managed relational DB (no OS access); **DynamoDB** = managed NoSQL (flexible schema)
- **Data in = free; Data out = costs money**
- **Paid support plans** = human help; **free resources** = self-service only
