(() => {
    const root = document.documentElement;
    const systemTheme = window.matchMedia("(prefers-color-scheme: dark)");
    const themeKey = "employee-directory-theme";

    function applyTheme(preference) {
        if (preference === "light" || preference === "dark") {
            root.dataset.theme = preference;
        } else {
            delete root.dataset.theme;
        }
    }

    try {
        applyTheme(window.localStorage.getItem(themeKey));
    } catch {
        // Storage can be unavailable. The system preference still applies.
    }

    function setupThemeToggle() {
        const toggle = document.querySelector("[data-theme-toggle]");

        function isDark() {
            return root.dataset.theme ? root.dataset.theme === "dark" : systemTheme.matches;
        }

        function updateToggle() {
            toggle.setAttribute("aria-pressed", String(isDark()));
        }

        toggle.addEventListener("click", () => {
            const preference = isDark() ? "light" : "dark";
            applyTheme(preference);
            updateToggle();
            try {
                window.localStorage.setItem(themeKey, preference);
            } catch {
                // The selected theme still works on the current page.
            }
        });

        systemTheme.addEventListener("change", updateToggle);
        window.addEventListener("storage", (event) => {
            if (event.key === themeKey || event.key === null) {
                applyTheme(event.newValue);
                updateToggle();
            }
        });

        updateToggle();
        toggle.hidden = false;
    }

    function setupDeleteDialog() {
        const dialog = document.querySelector("[data-delete-dialog]");
        if (typeof dialog?.showModal !== "function") {
            return;
        }

        const name = dialog.querySelector("[data-delete-name]");
        let selectedForm;

        for (const form of document.querySelectorAll("[data-delete-form]")) {
            const trigger = form.querySelector("[data-delete-open]");
            trigger.addEventListener("click", () => {
                selectedForm = form;
                name.textContent = form.dataset.employeeName;
                dialog.returnValue = "";
                dialog.showModal();
            });
            form.querySelector("[data-delete-fallback]").hidden = true;
            trigger.hidden = false;
        }

        dialog.addEventListener("close", () => {
            if (dialog.returnValue === "delete") {
                selectedForm.requestSubmit();
            }
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        setupThemeToggle();
        setupDeleteDialog();
    });
})();
