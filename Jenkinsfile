pipeline {
  agent any

  environment {
    EMAIL_RECIPIENT = 'abdelkarim.arouchi@esprit.tn'
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/Karimarouchi/devops.git'
      }
    }

    stage('SAST - Semgrep') {
      steps {
        sh '''
          echo "=== Running Semgrep ==="
          pip install semgrep
          semgrep --config=.semgrep/java-password-sql.yml --json -o semgrep-report.json || true
        '''
      }
    }

    stage('SCA - OWASP Dependency-Check') {
      steps {
        sh '''
          echo "=== Running OWASP Dependency-Check ==="
          wget https://github.com/jeremylong/DependencyCheck/releases/download/v10.0.3/dependency-check-10.0.3-release.zip
          unzip -q dependency-check-10.0.3-release.zip
          mv dependency-check /opt/dependency-check
          /opt/dependency-check/bin/dependency-check.sh --project "DevSecOps-Demo" --scan ./ --format "ALL" --out dependency-report || true
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
          docker run -v $(pwd):/zap/wrk/:rw ghcr.io/zaproxy/zaproxy:stable zap-baseline.py -t http://testphp.vulnweb.com -r zap-report.html || true
        '''
      }
    }

    stage('Secrets - Gitleaks') {
      steps {
        sh '''
          echo "=== Running Gitleaks ==="
          curl -sSL https://github.com/gitleaks/gitleaks/releases/latest/download/gitleaks_8.18.0_linux_x64.tar.gz | tar xz
          ./gitleaks detect --source . --report-format json --report-path gitleaks-report.json || true
        '''
      }
    }

    stage('SonarQube Analysis') {
      environment {
        SONAR_TOKEN = credentials('sonar-token')
      }
      steps {
        sh '''
          echo "=== Running SonarQube ==="
          sonar-scanner \
            -Dsonar.projectKey=DevSecOps-Demo \
            -Dsonar.sources=src \
            -Dsonar.host.url=http://localhost:9000 \
            -Dsonar.login=$SONAR_TOKEN
        '''
      }
    }
  }

  post {
    always {
      echo "=== Archiving reports ==="
      archiveArtifacts artifacts: '**/*.json, **/*.html, **/*.txt', fingerprint: true
    }
    failure {
      echo "=== Sending failure email ==="
      mail to: "${EMAIL_RECIPIENT}",
           subject: "❌ Jenkins DevSecOps Pipeline Failed",
           body: "Le pipeline DevSecOps a échoué. Consulte Jenkins pour les rapports détaillés."
    }
    success {
      mail to: "${EMAIL_RECIPIENT}",
           subject: "✅ Jenkins DevSecOps Pipeline Success",
           body: "Le pipeline DevSecOps s'est exécuté avec succès. Tous les rapports sont disponibles dans Jenkins."
    }
  }
}
