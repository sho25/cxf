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
name|tools
operator|.
name|corba
operator|.
name|common
operator|.
name|idltypes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|IdlEnum
extends|extends
name|IdlScopeBase
implements|implements
name|IdlType
block|{
specifier|private
name|IdlEnum
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlEnum
name|create
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|IdlEnum
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|addEnumerator
parameter_list|(
name|IdlEnumerator
name|e
parameter_list|)
block|{
name|addToScope
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
name|pw
operator|.
name|println
argument_list|(
name|indent
argument_list|()
operator|+
literal|"enum "
operator|+
name|localName
argument_list|()
operator|+
literal|" {"
argument_list|)
expr_stmt|;
name|indentMore
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|IdlDefn
argument_list|>
name|enums
init|=
name|definitions
argument_list|()
decl_stmt|;
name|int
name|needComma
init|=
name|enums
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
name|Iterator
argument_list|<
name|IdlDefn
argument_list|>
name|it
init|=
name|enums
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|IdlEnumerator
name|en
init|=
operator|(
name|IdlEnumerator
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|pw
operator|.
name|print
argument_list|(
name|indent
argument_list|()
operator|+
name|CorbaUtils
operator|.
name|mangleEnumIdentifier
argument_list|(
name|localName
argument_list|()
operator|+
literal|"_"
operator|+
name|en
operator|.
name|localName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|needComma
operator|--
operator|!=
literal|0
condition|)
block|{
name|pw
operator|.
name|println
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|pw
operator|.
name|println
argument_list|()
expr_stmt|;
name|indentLess
argument_list|()
expr_stmt|;
name|pw
operator|.
name|println
argument_list|(
name|indent
argument_list|()
operator|+
literal|"};"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

