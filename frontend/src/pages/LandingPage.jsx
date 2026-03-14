import { Link } from "react-router-dom";

function LandingPage() {
  return (
    <div style={{ fontFamily: "Arial, sans-serif" }}>
      
      {/* Navbar */}
      <nav style={styles.nav}>
        <h2>SplitWise AI</h2>
        <div>
          <Link to="/login" style={styles.link}>Login</Link>
          <Link to="/register" style={styles.button}>Sign Up</Link>
        </div>
      </nav>

      {/* Hero Section */}
      <section style={styles.hero}>
        <h1>Split Expenses Smarter with AI</h1>
        <p>
          Upload your bill, let AI extract the total, and split it instantly among friends.
        </p>
        <Link to="/register" style={styles.ctaButton}>
          Get Started
        </Link>
      </section>

      {/* Features Section */}
      <section style={styles.features}>
        <div style={styles.card}>
          <h3>📷 Bill Scanner</h3>
          <p>Upload receipt and auto-detect total amount.</p>
        </div>

        <div style={styles.card}>
          <h3>⚖ Smart Splitting</h3>
          <p>Equal, exact, or percentage-based expense division.</p>
        </div>

        <div style={styles.card}>
          <h3>💳 Simplified Settlements</h3>
          <p>Automatically reduce unnecessary transactions.</p>
        </div>
      </section>

    </div>
  );
}

const styles = {
  nav: {
    display: "flex",
    justifyContent: "space-between",
    padding: "20px",
    backgroundColor: "#f5f5f5",
    alignItems: "center"
  },
  link: {
    marginRight: "15px",
    textDecoration: "none",
    color: "black"
  },
  button: {
    textDecoration: "none",
    padding: "8px 16px",
    backgroundColor: "#4CAF50",
    color: "white",
    borderRadius: "5px"
  },
  hero: {
    textAlign: "center",
    padding: "80px 20px"
  },
  ctaButton: {
    display: "inline-block",
    marginTop: "20px",
    padding: "12px 24px",
    backgroundColor: "#4CAF50",
    color: "white",
    textDecoration: "none",
    borderRadius: "6px"
  },
  features: {
    display: "flex",
    justifyContent: "center",
    gap: "20px",
    padding: "40px"
  },
  card: {
    border: "1px solid #ddd",
    padding: "20px",
    borderRadius: "8px",
    width: "250px",
    textAlign: "center"
  }
};

export default LandingPage;