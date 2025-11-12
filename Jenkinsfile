pipeline {
  agent any

  environment {
    EMAIL_RECIPIENT = 'abdelkarim.arouchi@esprit.tn'
    PATH = "$PATH:/var/lib/jenkins/.local/bin"
  }

  stages {
    stage('SAST - Semgrep') {
      steps {
        sh '''
          echo "=== Running Semgrep ==="
          pip install --user semgrep
          ~/.local/bin/semgrep --config=.semgrep/java-password-sql.yml --json -o semgrep-report.json || true
        '''
      }
    }

    stage('SCA - OWASP Dependency-Check') {
      steps {
        sh '''
          echo "=== Running OWASP Dependency-Check ==="
          wget https://github.com/jeremylong/DependencyCheck/releases/download/v10.0.3/dependency-check-10.0.3-release.zip
          unzip -q dependency-check-10.0.3-release.zip
          mv dependency-check $WORKSPACE/dependency-check
          $WORKSPACE/dependency-check/bin/dependency-check.sh --project "DevSecOps-Demo" --scan ./ --format "ALL" --out dependency-check-report
        '''
      }
    }

    stage('Docker Scan - Trivy') {
      steps {
        sh '''
          echo "=== Running Docker image scan ==="
          docker build -t devsecops-demo:latest .
          docker run --rm aquasec/trivy image devsecops-demo:latest > docker-scan.txt || true
        '''
      }
    }

    stage('DAST - OWASP ZAP') {
      steps {
        sh '''
          echo "=== Running OWASP ZAP ==="
          docker run -v $(pwd):/zap/wrk/:rw ghcr.io/zaproxy/zaproxy:stable zap-baseline.py -t http://testphp.vulnweb.com -r zap_report.html || true
        '''
      }
    }

    stage('Secrets - Gitleaks') {
      steps {
        sh '''
          echo "=== Running Gitleaks ==="
          curl -sSL https://github.com/gitleaks/gitleaks/releases/latest/download/gitleaks_8.18.0_linux_x64.tar.gz | tar -xz
          ./gitleaks detect --source . --report-format json --report-path gitleaks-report.json || true
        '''
      }
    }

    // ❌ SonarQube désactivé pour l'instant
    // stage('SonarQube Analysis') { ... }
  }

  post {
    failure {
      echo "=== Pipeline failed ==="
    }
  }
}
