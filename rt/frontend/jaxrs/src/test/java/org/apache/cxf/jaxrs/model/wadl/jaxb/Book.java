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
name|model
operator|.
name|wadl
operator|.
name|jaxb
package|;
end_package

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
name|ws
operator|.
name|rs
operator|.
name|Produces
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
name|core
operator|.
name|MultivaluedMap
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
name|XmlElement
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
name|XmlType
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|Description
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|FormInterface
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
name|jaxrs
operator|.
name|model
operator|.
name|wadl
operator|.
name|XMLName
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"thebook"
argument_list|,
name|namespace
operator|=
literal|"http://superbooks"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"book"
argument_list|,
name|namespace
operator|=
literal|"http://superbooks"
argument_list|)
annotation|@
name|Description
argument_list|(
literal|"Book subresource"
argument_list|)
annotation|@
name|XMLName
argument_list|(
name|value
operator|=
literal|"{http://books}thesuperbook"
argument_list|)
specifier|public
class|class
name|Book
implements|implements
name|FormInterface
block|{
specifier|private
name|int
name|id
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"thechapter"
argument_list|,
name|namespace
operator|=
literal|"http://superbooks"
argument_list|)
specifier|private
name|Chapter
name|chapter
decl_stmt|;
specifier|public
name|Book
parameter_list|()
block|{     }
specifier|public
name|Book
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/book"
argument_list|)
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
annotation|@
name|Description
argument_list|(
literal|"Get the book"
argument_list|)
specifier|public
name|Book
name|getIt
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|ident
parameter_list|)
block|{
name|id
operator|=
name|ident
expr_stmt|;
block|}
specifier|public
name|int
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
literal|"/chapter/{cid}"
argument_list|)
specifier|public
name|Chapter
name|getChapter
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"cid"
argument_list|)
name|int
name|cid
parameter_list|)
block|{
return|return
name|chapter
return|;
block|}
specifier|public
name|void
name|form1
parameter_list|(
name|MultivaluedMap
name|map
parameter_list|)
block|{     }
specifier|public
name|String
name|form2
parameter_list|(
name|String
name|f1
parameter_list|,
name|String
name|f2
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
specifier|public
name|String
name|form3
parameter_list|(
name|String
name|identificator
parameter_list|,
name|String
name|f1
parameter_list|,
name|String
name|f2
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

