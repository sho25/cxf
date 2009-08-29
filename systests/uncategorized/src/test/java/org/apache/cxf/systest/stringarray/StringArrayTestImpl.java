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
name|systest
operator|.
name|stringarray
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|stringarray
operator|.
name|StringListTest
import|;
end_import

begin_class
specifier|public
class|class
name|StringArrayTestImpl
implements|implements
name|StringListTest
block|{
specifier|public
name|String
index|[]
name|stringListTest
parameter_list|(
name|String
index|[]
name|in
parameter_list|)
block|{
for|for
control|(
name|String
name|str
range|:
name|in
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--str-- "
operator|+
name|str
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

