function ValidateData()
      {
        var emailStr = document.getElementById("emailStr").value;
        var phoneStr = document.getElementById("phoneStr").value;

        var phoneformat = /^\+?([0-9]{2})\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})$/;
        var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if(!emailStr.value.match(mailformat))
          {
            alert("You have entered an invalid email address!");
            return false;
          }
        if(!phoneStr.value.match(phoneformat))
        {
          alert("You have entered an invalid phone number");
          return false;
        }
        return false;
      }