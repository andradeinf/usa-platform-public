**Background**

The UsaPlatform (originally called “Sistema para Franquias”) is the longest software development project I was involved in my professional career. It all started as an idea from my brother in 2014 and it lasted for almost 9 years, from the conception to the retirement that happened in 2022. It was always a side project, executed hours after hours in my personal time with the idea to grow and become a nice platform for the Franchising market to manage their internal needs. In the beginning it was a simple tool to order “branded” products, but grew year after year, with more and more functionality to support other needs, like internal messaging, notifications, file sharing, training, etc. At some point it got quite some traction, with some 10s of clients and 100s of monthly users, but priorities changed on the way and also the fact that it never scaled to become the “main job” made us clearly see how difficult it was to keep maintaining a platform, upgrading technical components and add more features as a side job, up to the point that both, the technical debts and the changes in life priority made us realise we should not continue. The platform was built in a very generic way, so it almost changed market focus many times. We even started incubation with an AI incubator in Porto Alegre (RS, Brazil) and won a prize from the Federal University in Rio Grande do Sul (UFRGS) with the idea to build an AI supported platform to centralise buyers and sellers in a single place and be able to lower price with bigger volume purchases. Not even that was enough to make the market, unfortunately.

As a technical project, though, it was a big learning and I consider a huge success for me, personally. As a “solo project”, every line of code, any technical decision, any design and architectural change and all the technology learnings were done by a single person: me. It was a very nice journey to learn how to build a fully serverless solution taking advantage of the (initially) free Google Cloud Platform services as Google Data Store, File Storage and Application Engine. It was also a reason to learn how to build an entire full stack application, with the proper backend APIs and tweak each pixel in the Frontend to have it running in a nice way. I have learnt a lot of Java, migrated a couple of times the ORM used to persist data, the DevOps tools to build and deploy the app, as well as all the Frontend things: learn AngularJS (Yes, first version of Angular) from scratch, with all the HTML, CSS and JS required to build something fair to be used with some good user experience. But it was not only technical, for sure. There was countless hours of functional discussions, scope definition, customer inputs, POCs, trying and error, troubleshooting and practicing multiple other skills that made me grow a lot as a professional, applying things that I learnt in my “official” job, but also learning new things that later on I could also apply in my “official job”.

As any code, it is not perfect. It is actually far from it. It has a lot of technical debts, things that I always said “later on I will fix” and never had time to do it, and bad decisions taken on the way that, due to multiple different constraints, I could never go back and improve. It uses also a lot off “old stuff”: AngularJS is not a thing anymore for a while, but I was too much in it to be able to rebuild everything myself, Spring Framework old version that I could never replace, fearing it would break something and I would not have time to fix, just to mention a few. In addition to that, that very old saying “do not touch what is working” also applied here. I can proudly say that, while it was running, it was very stable, had very little support needed and was able to deliver quite some value for the customers that were using it. It also proved to have a decent architecture, allowing me to evolve and Ade new functionality multiple times on the way. It ran for many years with very low cost and in a very reliable way, what makes me pleased.

**Why am I making the repo public right now?**

During my entire career I lost track of how many projects or systems I have built, but all the code was private property and I could not share publicly. The code here, though, I have built to a personal project and that allows me to share it freely.
In addition to that, I’m very proud of all the work I have done and also feel that it can help to show what I am able to achieve as a “one man team”. It is a proven record that I can create nice value via many functionalities added to the application and keep a full project live for so long from beginning to end.
You can see here also how (well or not, judge it yourself) I can build software. You can see a bit of my decisions in terms of organizing code, separate concerns and build a scalable complex application. I ask you, though, to take it with a proper mindset: this project was built as a side project, throughout multiple hours of personal time and with a lot of circumstances such an environment can bring. Sometimes I was not touching the codebase for months, what makes difficult to keep things in mind once you are back. Most of the time I had 1-2 hours in a day, so constantly switching context also did not help much here. And finally, this project started so many years ago and evolved for so long with restricted resources that it uses quite some old frameworks and technologies and a lot of things could never be improved/fixed/updated.


