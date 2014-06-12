# Android Cursor Utils

Working with Android SQLite `Cursor`s is less than ideal. Code can easily be duplicated throughout your codebase if you're not careful to keep the translation of SQLite columns into Java objects. Iterating across the returned data from a `Cursor` is another area which can lead to code duplication. Sometimes you actually want a `Cursor` to be a `List`, yet other times a `List` to be a `Cursor`.
 
This library was designed to encapsulate the repeatable actions for Android `Cursor`s and treat them as if they were closer to a Java object rather than a conglomerate of `dict`s/`hash`es.
 
## IterableCursor (and IterableCursorWrapper)

At minimum, you should use an `IterableCursorWrapper` and never look at a plain old Android `Cursor` again.

```java
public class User {
    public User(String name, String bio) { /* ... */ }
}

public class UserCursor extends IterableCursorWrapper<User> {

    public UserCursor(Cursor c) {
        super(c);
    }

    public User peek() {
        String name = getStringHelper(COLUMN_USER_NAME, "Default Name");
        String bio = getLongHelper(COLUMN_USER_BIO, "No bio yet");
        return new User(name, bio);
    }
    
}

public class MyDatabase extends SQLiteOpenHelper {

    // ...
    
    public IterableCursor<User> queryAllUsers() {
        Cursor cursor = getReadableDatabase().query(TABLE_USERS, null, null, null, null, null,
                null, null);
        return new UserCursor(cursor);
    }
    
}
```

Voilà! Now you can use an enhanced `for` loop to access all `User`s at once:

```java
IterableCursor<User> users = myDb.queryAllUsers();
for (User user : users) {
    if (user.isMyBestFriend()) giveSomeCake(user);
}
users.close();
```

This also simplifies using a `CursorAdapter`:

```java
@Override
public void bindView(View v, Context context, Cursor cursor) {
    User user = ((IterableCursor<User>) cursor).peek();
    
    ViewHolder holder = v.getTag();
    holder.textView.setText(user.getFullName());
}
```

## CursorList

Often, certain queries are difficult to express in SQL constraints and it's much simpler to defer to Java to do filtering. But what happens when you're done filtering: you're left with a `List` but you want to use your favorite `CursorAdapter`. To solve this issue, mask your `List` as a `CursorList`:
 
```java
IterableCursor<User> users = myDb.queryAllUsers();
List<User> bestFriends = new LinkedList<User>();
for (User user : users) {
    if (user.isMyBestFriend()) bestFriends.add(user);
}

IterableCursor<User> bestFriendCursor = new CursorList(bestFriends);
bestFriendsListView.setAdapter(new UserCursorAdapter(bestFriendsCursor));
```

`CursorList` is an instance of both `android.database.Cursor` and `java.util.List`; it's the best of both worlds.
  
## IterableCursor + IterableCursor

What if you wanted to have one master `ListView` that prioritized best friends first, but then listed everyone alphabetically?
 
```java
List<User> bestFriendsList = computeBestFriendsFromCursor(); // see above
IterableCursor<User> bestFriends = new CursorList(bestFriendsList);
IterableCursor<User> allUsers = myDb.queryAllUsers();

IterableCursor<User> mergedCursor = new IterableMergeCursor<User>(bestFriends, allUsers);
listView.setAdapter(new UserCursorAdapter(mergedCursor));
```

The adapter has no idea that it's getting two cursors - it still gets to `peek()` for a `User` and doesn't need to know that you already did some sorting.

## Cursor helper methods

Getting data at a particular column of a `Cursor` is a pain. And what happens if you have a custom query that only returns certain columns and not the full row?
 
```java
String field1 = cursor.getString(cursor.getColumnIndex(COLUMN_FIELD1));
int field2 = cursor.getInt(cursor.getColumnIndex(COLUMN_FIELD2));
try {
    boolean field3 = cursor.getBoolean(cursor.getColumnIndex(COLUMN_FIELD3));
} catch (Exception) {
    // oh no, field3 wasn't returned this time! what should I do???
}
// ...
```

`IterableCursorWrapper` provides helper methods which remove this headache with simpler methods and default values. 

```java
public User peek() {
    String field1 = getStringHelper(COLUMN_FIELD1, "default");
    int field2 = getIntegerHelper(COLUMN_FIELD2, 0);
    boolean field3 = getBooleanHelper(COLUMN_FIELD3, DEFAULT_FIELD3_VALUE);
}
```

## Cursor &rarr; Collection
 
If the cursor size isn't too big and you just want to deal with a `Collection` instead, `CursorUtils` has some methods to translate the `IterableCursor` into your favorite collection:

```java
IterableCursor<User> cursor1 = queryAllUsers();
ArrayList<User> all = CursorUtils.consumeToArrayList(cursor1);
// also comes with consumeToLinkedList()

IterableCursor<Tweet> cursor2 = queryAllTweetsAndRetweets();
LinkedHashSet<User> noRepeats = CursorUtils.consumeToLinkedHashSet(cursor2);

IterableCursor<GooglePlusUser> cursor3 = queryComplicatedDataSet();
CoolGuavaCollection<GooglePlusUser> hashMultiQueue = Guava.newAwesome();
CursorUtils.consumeToCollection(hashMultiQueue, cursor3);
```

## Download

Gradle:
```groovy
compile 'com.venmo.cursor:library:0.1'
```

Maven:
```xml
<dependency>
  <groupId>com.venmo.cursor>
  <artifactId>library</artifactId>
  <version>0.1</version>
</dependency>
```

Jar: [[direct download link]][1]

[1]: http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.venmo.cursor&a=library&v=LATEST

## Contributing

We'd love to see your ideas for improving this library! The best way to contribute is by submitting a pull request – we'll do our best to respond to your patch as soon as possible. You can also [submit an issue](https://github.com/venmo/cursor-utils/issues/new) if you find bugs or have any questions. :octocat:

Please make sure to follow our general coding style and add test coverage for new features!
