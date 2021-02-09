package com.soufianekre.smallhub.helper;

public class ShemeParser {

    /*

    public static void launchUri(@NonNull Context context, @NonNull String url) {
        launchUri(context, Uri.parse(url), false);
    }

    public static void launchUri(@NonNull Context context, @NonNull Uri data) {
        launchUri(context, data, false);
    }

    public static void launchUri(@NonNull Context context, @NonNull Uri data, boolean showRepoBtn) {
        launchUri(context, data, showRepoBtn, false);
    }

    public static void launchUri(@NonNull Context context, @NonNull Uri data, boolean showRepoBtn, boolean newDocument) {
        //Logger.e(data);
        Intent intent = convert(context, data, showRepoBtn);
        if (intent != null) {
            intent.putExtra(BundleConstant.SCHEME_URL, data.toString());
            if (newDocument) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            if (context instanceof Service || context instanceof Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } else {
            Activity activity = ActivityHelper.getActivity(context);
            if (activity != null) {
                ActivityHelper.startCustomTab(activity, data);
            } else {
                ActivityHelper.openChooser(context, data);
            }
        }
    }

    @Nullable
    private static Intent convert(@NonNull Context context, Uri data, boolean showRepoBtn) {
        if (data == null) return null;
        if (InputHelper.isEmpty(data.getHost()) || InputHelper.isEmpty(data.getScheme())) {
            String host = data.getHost();
            if (InputHelper.isEmpty(host)) host = HOST_DEFAULT;
            String scheme = data.getScheme();
            if (InputHelper.isEmpty(scheme)) scheme = PROTOCOL_HTTPS;
            String prefix = scheme + "://" + host;
            String path = data.getPath();
            if (!InputHelper.isEmpty(path)) {
                if (path.charAt(0) == '/') {
                    data = Uri.parse(prefix + path);
                } else {
                    data = Uri.parse(prefix + '/' + path);
                }
            } else {
                data = Uri.parse(prefix);
            }
        }
        if (data.getPathSegments() != null && !data.getPathSegments().isEmpty()) {
            if (IGNORED_LIST.contains(data.getPathSegments().get(0))) return null;
            return getIntentForURI(context, data, showRepoBtn);
        }
        return null;
    }

    @Nullable private static Intent getRepo(@NonNull Context context, @NonNull Uri uri) {
        List<String> segments = uri.getPathSegments();
        if (segments == null || segments.size() < 2 || segments.size() > 3) return null;
        String owner = segments.get(0);
        String repoName = segments.get(1);
        if (!InputHelper.isEmpty(repoName)) {
            if (repoName.endsWith(".git")) repoName = repoName.replace(".git", "");
        }
        if (segments.size() == 3) {
            String lastPath = uri.getLastPathSegment();
            if ("milestones".equalsIgnoreCase(lastPath)) {
                return RepoPagerActivity.createIntent(context, repoName, owner, CODE, 4);
            } else if ("network".equalsIgnoreCase(lastPath)) {
                return RepoPagerActivity.createIntent(context, repoName, owner, CODE, 3);
            } else if ("stargazers".equalsIgnoreCase(lastPath)) {
                return RepoPagerActivity.createIntent(context, repoName, owner, CODE, 2);
            } else if ("watchers".equalsIgnoreCase(lastPath)) {
                return RepoPagerActivity.createIntent(context, repoName, owner, CODE, 1);
            } else if ("labels".equalsIgnoreCase(lastPath)) {
                return RepoPagerActivity.createIntent(context, repoName, owner, CODE, 5);
            } else {
                return null;
            }
        } else {
            return RepoPagerActivity.createIntent(context, repoName, owner);
        }
    }


     */
}
