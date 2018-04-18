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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|fortest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|ns2
operator|.
name|ObjectWithAMapNs2
import|;
end_import

begin_class
specifier|public
class|class
name|MapTestImpl
implements|implements
name|MapTest
block|{
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getMapStringToLong
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"twenty-seven"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
literal|27
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|public
name|void
name|takeMap
parameter_list|(
name|ObjectWithAMap
name|map
parameter_list|)
block|{     }
specifier|public
name|ObjectWithAMap
name|returnObjectWithAMap
parameter_list|()
block|{
name|ObjectWithAMap
name|ret
init|=
operator|new
name|ObjectWithAMap
argument_list|()
decl_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"rainy"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"raw"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"sunny"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|getMapLongToString
parameter_list|()
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|27
argument_list|)
argument_list|,
literal|"twenty-seven"
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|public
name|ObjectWithAMapNs2
name|returnObjectWithAMapNs2
parameter_list|()
block|{
name|ObjectWithAMapNs2
name|ret
init|=
operator|new
name|ObjectWithAMapNs2
argument_list|()
decl_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"rainy"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"sunny"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|ret
operator|.
name|getTheMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"cloudy"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|takeMapNs2
parameter_list|(
name|ObjectWithAMapNs2
name|map
parameter_list|)
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getRawMapStringToInteger
parameter_list|()
block|{
name|Map
name|r
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|r
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|12
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

