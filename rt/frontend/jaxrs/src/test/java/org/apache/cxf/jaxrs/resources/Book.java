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
name|resources
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"Book"
argument_list|)
specifier|public
class|class
name|Book
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Chapter
argument_list|>
name|chapters
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Chapter
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{     }
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"chapters/{chapterid}/"
argument_list|)
annotation|@
name|GET
specifier|public
name|Chapter
name|getChapter
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|int
name|chapterid
parameter_list|)
block|{
return|return
name|chapters
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|chapterid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

