begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|basic
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|JsonMapObjectReaderWriterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWriteMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"aValue"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"cValue"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"claim"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|json
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|toJson
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"a\":\"aValue\",\"b\":123,\"c\":[\"cValue\"],\"claim\":null}"
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteDateProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|date
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"createdAt"
argument_list|,
name|date
argument_list|)
decl_stmt|;
name|String
name|json
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|toJson
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"createdAt\":\""
operator|+
name|date
operator|.
name|toString
argument_list|()
operator|+
literal|"\"}"
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadMap
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"a\":\"aValue\",\"b\":123,\"c\":[\"cValue\"],\"f\":null}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|fromJson
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"cValue"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"f"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadMapWithValueCommas
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"a\":\"aValue1,aValue2\",\"b\":\"bValue1\"\r\n,\"c\":[\"cValue1, cValue2\"],"
operator|+
literal|"\"d\":\"dValue1,dValue2,dValue3,dValue4\"}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|fromJson
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue1,aValue2"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bValue1"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"cValue1, cValue2"
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dValue1,dValue2,dValue3,dValue4"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadStringWithLeftCurlyBracketInString
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"x\":{\"y\":\"{\"}}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|xMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|xMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadStringWithLeftCurlyBracketInString2
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"x\":{\"y\":\"{\", \"z\":\"{\"}, \"a\":\"b\"}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|xMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|xMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadStringWithRightCurlyBracketInString
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"x\":{\"y\":\"}\"}}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|xMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|xMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"}"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadStringWithRightCurlyBracketInString2
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"x\":{\"y\":\"}\", \"z\":\"}\"}, \"a\":\"b\"}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|xMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|xMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"}"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"}"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadStringWithCurlyBracketsInString
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"x\":{\"y\":\"{\\\"}\"}}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|xMap
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|xMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\\\"}"
argument_list|,
name|xMap
operator|.
name|get
argument_list|(
literal|"y"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEscapedForwardSlashInString
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonMapObjectReaderWriter
name|jsonMapObjectReaderWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"{\"kid\":\"4pZbe4shQQGzZXHbeIlbDvmHOc1\\/H6jH6oBk3nUrcZE=\",\"alg\":\"RS256\"}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jsonMapObjectReaderWriter
operator|.
name|fromJson
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|kid
init|=
operator|(
name|String
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"kid"
argument_list|)
decl_stmt|;
name|String
name|expectedKid
init|=
literal|"4pZbe4shQQGzZXHbeIlbDvmHOc1/H6jH6oBk3nUrcZE="
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedKid
argument_list|,
name|kid
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

