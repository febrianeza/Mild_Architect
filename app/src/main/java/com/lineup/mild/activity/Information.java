package com.lineup.mild.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lineup.mild.R;
import com.lineup.mild.databinding.ActivityInformationBinding;
import com.lineup.mild.util.Font;
import com.pepperonas.materialdialog.MaterialDialog;
import com.pepperonas.materialdialog.model.Changelog;
import com.pepperonas.materialdialog.model.LicenseInfo;
import com.pepperonas.materialdialog.model.ReleaseInfo;

import java.util.ArrayList;
import java.util.List;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInformationBinding binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.toolbarText.setTypeface(new Font(this).nunitoSans());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    public void infoClick(View view) {
        switch (view.getId()) {
            case R.id.mChangelog:
                List<Changelog> changelogs = getChangelog();
                new MaterialDialog.Builder(Information.this)
                        .title("Changelog")
                        .changelogDialog(changelogs, getString(R.string.bullet_release_info))
                        .positiveText("Ok")
                        .show();
                break;
            case R.id.mPrivacyPolicy:
                Uri uri = Uri.parse(getResources().getString(R.string.private_policy_url));
                Intent toUrl = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(toUrl);
                break;
            case R.id.mLicenses:
                List<LicenseInfo> licenseInfos = getLicenseInfos();
                new MaterialDialog.Builder(Information.this)
                        .title("Licenses")
                        .licenseDialog(licenseInfos)
                        .positiveText("Ok")
                        .show();
                break;
            case R.id.mCopyright:
                new MaterialDialog.Builder(Information.this)
                        .title("Copyright")
                        .message(R.string.copyright)
                        .positiveText("OK")
                        .show();
                break;
            case R.id.mBugReport:
                new MaterialDialog.Builder(Information.this)
                        .title("Bug Report")
                        .message(R.string.bug_report)
                        .positiveText("OK")
                        .show();
                break;
        }
    }

    private List<LicenseInfo> getLicenseInfos() {
        List<LicenseInfo> licenseInfos = new ArrayList<>();
        licenseInfos.add(new LicenseInfo(
                "App Icon",
                "Icon by DinosoftLabs",
                "https://www.flaticon.com/authors/dinosoftlabs"
        ));
        licenseInfos.add(new LicenseInfo(
                "Picasso",
                "Copyright 2013 Square, Inc.",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "Retrofit",
                "Copyright 2013 Square, Inc.",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "OkHttp",
                "Copyright 2016 Square, Inc.",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "Dexter",
                "Copyright 2015 Karumi",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "MaterialDrawer",
                "Copyright 2018 Mike Penz",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "Android-Iconics",
                "Copyright 2018 Mike Penz",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "ExpandableLayout",
                "Copyright 2016 Daniel Cachapa.",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));
        licenseInfos.add(new LicenseInfo(
                "MaterialDialog",
                "Copyright 2017 Martin Pfeffer",
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License."
        ));

        return licenseInfos;
    }

    private List<Changelog> getChangelog() {
        List<Changelog> changelogs = new ArrayList<>();

        changelogs.add(new Changelog("2.0.3", "2021-02-12", new ReleaseInfo(
                "Minor UI change",
                "Performance improvements",
                "Server-side improvements"
        )));

        changelogs.add(new Changelog("2.0.1", "2019-03-07", new ReleaseInfo(
                "Server-side improvements",
                "Change recent image layout"
        )));

        changelogs.add(new Changelog("2.0.0", "2018-12-20", new ReleaseInfo(
                "Change app name to Mild Architect",
                "Spesific category to Architectural Wallpaper only",
                "Major change on server-side improvements",
                "Device support only to Android 21+",
                "Image up to 4K Resolution"
        )));
        changelogs.add(new Changelog("1.0.5", "2018-11-04", new ReleaseInfo("Performance improvement")));
        changelogs.add(new Changelog("1.0.3", "2018-04-12", new ReleaseInfo("Download Count Bug fixes")));
        changelogs.add(new Changelog("1.0.2", "2017-11-26", new ReleaseInfo("Minor bug fixes", "Server-side improvements")));
        changelogs.add(new Changelog("1.0.1", "2017-10-31", new ReleaseInfo("Bug fixes and performance improvement")));
        changelogs.add(new Changelog("1.0.0", "2017-10-30", new ReleaseInfo("Initial Release")));
        return changelogs;
    }
}
