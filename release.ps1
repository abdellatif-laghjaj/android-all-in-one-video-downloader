# Windows alternative to release.sh — bumps the version, tags, and pushes so
# GitHub Actions builds and publishes the release.
#
# Usage (from the project root):
#   .\release.ps1 2.0.2

param(
    [Parameter(Mandatory = $true, Position = 0)]
    [ValidatePattern('^\d+\.\d+\.\d+$')]
    [string]$VersionName
)

$ErrorActionPreference = 'Stop'

$Tag = "v$VersionName"
$GradleFile = "app\build.gradle.kts"

if (-not (Test-Path $GradleFile)) {
    Write-Error "ERROR: $GradleFile not found. Run this from the project root."
    exit 1
}

$existingTag = git tag --list $Tag
if ($existingTag) {
    Write-Error "ERROR: tag $Tag already exists."
    exit 1
}

$content = [System.IO.File]::ReadAllText((Resolve-Path $GradleFile))

$codeMatch = [regex]::Match($content, 'versionCode = (\d+)')
if (-not $codeMatch.Success) {
    Write-Error "ERROR: versionCode not found in $GradleFile."
    exit 1
}
$currentVersionCode = [int]$codeMatch.Groups[1].Value
$newVersionCode = $currentVersionCode + 1

Write-Host "versionCode: $currentVersionCode -> $newVersionCode"
Write-Host "versionName: -> $VersionName"

$content = $content -replace 'versionCode = \d+', "versionCode = $newVersionCode"
$content = $content -replace 'versionName = "[^"]*"', "versionName = `"$VersionName`""

# WriteAllText keeps UTF-8 without BOM, matching what Gradle expects.
[System.IO.File]::WriteAllText((Resolve-Path $GradleFile), $content)

git add $GradleFile
if ($LASTEXITCODE -ne 0) { exit 1 }
git commit -m "chore: release $Tag"
if ($LASTEXITCODE -ne 0) { exit 1 }
git tag -a $Tag -m "Release $Tag"
if ($LASTEXITCODE -ne 0) { exit 1 }
git push origin HEAD
if ($LASTEXITCODE -ne 0) { exit 1 }
git push origin $Tag
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host ""
Write-Host "Pushed $Tag. GitHub Actions will now build, sign, and publish the release."