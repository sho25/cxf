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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Convert a "nonProxyHosts" formatted String into a usable regular expression usable in {@code Pattern}.   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PatternBuilder
block|{
comment|/**      * Default empty constructor fot utility class.      */
specifier|private
name|PatternBuilder
parameter_list|()
block|{ }
comment|/**      * Builds a {@code Pattern} from the given String argument.      * @param value pattern like expression      * @return a usable {@code Pattern}.      */
specifier|public
specifier|static
name|Pattern
name|build
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
comment|// Here is a usual nonProxyHosts value:
comment|// localhost|www.google.*|*.apache.org
comment|// '|' are separator for 'or' group
comment|// '.' are real dots
comment|// '*' means any char (length>= 0)
comment|// So we need to convert that value a little bit to make it a real regular expression suited for Java
name|String
name|regexp
init|=
name|value
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"\\."
argument_list|)
decl_stmt|;
name|regexp
operator|=
name|regexp
operator|.
name|replace
argument_list|(
literal|"*"
argument_list|,
literal|".*"
argument_list|)
expr_stmt|;
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|regexp
argument_list|)
return|;
block|}
block|}
end_class

end_unit

