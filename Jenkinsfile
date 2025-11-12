pipeline {
    agent any

    stages {
        stage('SAST - Semgrep') {
            steps {
                sh '''
                echo "=== Running Semgrep ==="
                pip install --user semgrep
                ~/.local/bin/semgrep --config=.semgrep/java-password-sql.yml --json -o semgrep-report.json
                '''
            }
        }

        stage('SCA - OWASP Dependency-Check') {
            steps {
                sh '''
                echo "=== Running OWASP Dependency-Check ==="
                wget -q https://github.com/jeremylong/DependencyCheck/releases/download/v10.0.3/dependency-check-10.0.3-release.zip -O dependency-check.zip
                rm -rf dependency-check
                unzip -o -q dependency-check.zip
                ./dependency-check/bin/dependency-check.sh --project "MyApp" --scan . --format JSON --out .
                '''
            }
        }

        stage('Docker Scan - Trivy') {
            steps {
                sh '''
                echo "=== Running Trivy ==="
                trivy fs . > trivy-report.txt || true
                '''
            }
        }

        stage('DAST - OWASP ZAP') {
            steps {
                sh '''
                echo "=== Running OWASP ZAP ==="
                docker run --rm -v $(pwd):/zap/wrk/:rw ghcr.io/zaproxy/zaproxy:stable zap-baseline.py -t http://example.com -r zap-report.html || true
                '''
            }
        }

        stage('Secrets - Gitleaks') {
            steps {
                sh '''
                echo "=== Running Gitleaks ==="
                docker run --rm -v $(pwd):/path zricethezav/gitleaks detect --source=/path --report-format json --report-path gitleaks-report.json || true
                '''
            }
        }
    }

    post {
        always {
            echo "=== Pipeline finished ==="
        }
        failure {
            echo "=== Pipeline failed ==="
        }
    }
}
